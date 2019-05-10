package com.robertsanek.data.etl.remote.scrape.indiehackers;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.robertsanek.data.etl.DoNotRun;
import com.robertsanek.data.etl.Etl;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@DoNotRun(explanation = "resource-hungry (uses multiple chromedrivers)")
public class IndieHackersBookEtl extends Etl<IndieHackersBook> {

  private static Log log = Logs.getLog(IndieHackersBookEtl.class);
  private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
  private static final Set<String> BOOK_DOMAINS = ImmutableSet.of(
      "amazon.com",
      "goodreads.com"
  );

  private int driverCount = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
  private Map<Long, RemoteWebDriver> drivers = LongStream.range(1, driverCount + 1)
      .boxed()
      .collect(Collectors.toMap(Function.identity(), i -> new ChromeDriver()));
  private Timer t = new Timer();

  @Override
  public List<IndieHackersBook> getObjects() throws Exception {
    List<IndieHackersBook> allBooks = Collections.synchronizedList(Lists.newArrayList());
    Set<URL> interviewURLs = getInterviewURLs();
    log.info("Visiting %s individual URLs to find mentions of books...", interviewURLs.size());
    ForkJoinPool pool = new ForkJoinPool(driverCount);
    setupLoggingForPool(pool);
    interviewURLs
        .forEach(articleUrl -> pool.submit(() -> {
          WebDriver driver = getThreadLocalDriver();
          driver.get(articleUrl.toString());
          new WebDriverWait(driver, 10).until(
              ExpectedConditions.presenceOfElementLocated(By.className("interview-body")));
          allBooks.addAll(
              driver.findElements(By.cssSelector("a")).stream()
                  .filter(a -> BOOK_DOMAINS.stream().anyMatch(domain -> a.getAttribute("href").contains(domain)))
                  .map(a -> IndieHackersBook.IndieHackersBookBuilder.anIndieHackersBook()
                      .withId(ID_GENERATOR.getAndIncrement())
                      .withUrl(a.getAttribute("href"))
                      .withAnchorText(a.getText())
                      .withIndieHackersUrl(articleUrl.toString())
                      .withIndieHackersKudos(Long.valueOf(
                          driver.findElements(By.cssSelector("div.thread-voter__count")).get(0).getText()))
                      .withIndieHackersTitle(driver.getTitle())
                      .build())
                  .collect(Collectors.toList()));
        }));
    pool.shutdown();
    pool.awaitTermination(10, TimeUnit.MINUTES);
    log.info("Successfully visited %s pages, gathering %s likely links to books. Shutting down remote drivers...",
        interviewURLs.size(), allBooks.size());
    drivers.values().forEach(RemoteWebDriver::close);
    t.cancel();

    return allBooks;
  }

  // We don't know the URLs of all the pages that list interviews (they're paginated, 20/page). Here we'll detect it by
  // waiting to see a redirect.
  private int getNumberOfInterviewListPages() throws InterruptedException {
    int guessPages = 20;
    log.info("Detecting total amount of pages of interviews... (initial guess: %s)", guessPages);
    while (true) {
      WebDriver driver = getThreadLocalDriver();
      URI interviewListUrl = getInterviewListUrl(guessPages);
      driver.get(interviewListUrl.toString());
      new WebDriverWait(driver, Duration.ofSeconds(10).getSeconds()).until(
          ExpectedConditions.presenceOfElementLocated(By.className("interviews__interviews")));
      Thread.sleep(Duration.ofSeconds(3).toMillis());
      String currentUrl = driver.getCurrentUrl();
      //Indie Hackers will redirect you to the last valid page if you go over
      if (Integer.valueOf(currentUrl.substring(currentUrl.length() - 2)) < guessPages) {
        guessPages--;
        log.info("Detected %s interview pages.", guessPages);
        return guessPages;
      }
      guessPages++;
    }
  }

  private void setupLoggingForPool(ForkJoinPool pool) {
    long startTime = System.nanoTime();
    AtomicLong totalTasks = new AtomicLong(0);
    t.schedule(new TimerTask() {
      @Override
      public void run() {
        int left = pool.getQueuedSubmissionCount();

        //via https://stackoverflow.com/a/30599099
        long elapsedTime = System.nanoTime() - startTime;
        totalTasks.set(Math.max(totalTasks.get(), left));
        long allTimeForDownloading = (elapsedTime * totalTasks.get() / Math.max(1, totalTasks.get() - left));
        long remainingTime = allTimeForDownloading - elapsedTime;
        if (left > 0) {
          log.info("%s URLs left to visit... (ETA %s)",
              left, DurationFormatUtils.formatDuration(Duration.ofNanos(remainingTime).toMillis(), "mm:ss"));
        }
      }
    }, 0, Duration.ofSeconds(15).toMillis());
  }

  private Set<URL> getInterviewURLs() throws InterruptedException {
    int pages = getNumberOfInterviewListPages();
    log.info("Visiting %s pages to gather links for specific interviews...", pages);
    Set<URL> interviews = Sets.newConcurrentHashSet();
    ExecutorService pool = new ForkJoinPool(driverCount);
    IntStream.rangeClosed(1, pages)
        .forEach(pageNumber -> pool.submit(() -> {
          WebDriver driver = getThreadLocalDriver();
          driver.get(getInterviewListUrl(pageNumber).toString());
          new WebDriverWait(driver, Duration.ofSeconds(10).getSeconds()).until(
              ExpectedConditions.presenceOfElementLocated(By.className("interviews__interviews")));
          interviews.addAll(driver.findElements(By.cssSelector("a.interview__link")).stream()
              .map(a -> Unchecked.get(() -> new URL(a.getAttribute("href"))))
              .collect(Collectors.toSet()));
        }));
    pool.shutdown();
    pool.awaitTermination(10, TimeUnit.MINUTES);
    log.info("Successfully gathered %s links to individual interviews.", interviews.size());

    return interviews;
  }

  private URI getInterviewListUrl(int pageNumber) {
    return Unchecked.get(() -> new URIBuilder()
        .setScheme("https")
        .setHost("indiehackers.com")
        .setPath(String.format("interviews/page/%s", pageNumber))
        .build());
  }

  private WebDriver getThreadLocalDriver() {
    return drivers.get(Thread.currentThread().getId() % driverCount + 1);
  }

}
