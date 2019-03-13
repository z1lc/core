package com.robertsanek;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.robertsanek.util.Unchecked;

public class ParallelExample {

  public static AtomicInteger exampleCounter = new AtomicInteger(0);

  public static void main(String[] args) {
    multipleIncrementExample();
  }

  public static void parallelSortExample() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    List<Integer> randomNumbers = Lists.newArrayList();
    for (int i = 0; i < 20_000_000; i++) {
      randomNumbers.add(random.nextInt());
    }

    Collections.shuffle(randomNumbers);

    Stopwatch stopwatch = Stopwatch.createStarted();
    List<Integer> sortedList = randomNumbers.stream()
        .sorted()
        .collect(Collectors.toList());
    System.out.println("Not parallel: " + stopwatch.elapsed().getSeconds());

    Collections.shuffle(randomNumbers);

    stopwatch.reset().start();
    List<Integer> sortedList2 = randomNumbers.parallelStream()
        .sorted()
        .collect(Collectors.toList());
    System.out.println("Parallel: " + stopwatch.elapsed().getSeconds());
  }

  public static void incrementCounter() {
    exampleCounter.incrementAndGet();
  }

  public static void multipleIncrementExample() {
    Runnable incrementCounter = new Runnable() {
      @Override
      public void run() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 100; i++) {
          int randomMilliSeconds = random.nextInt(0, 10);
          sleep(randomMilliSeconds);
          incrementCounter();
        }
      }
    };

    for (int i = 0; i < 10; i++) {
      new Thread(incrementCounter).start();
    }

    sleep(Duration.ofSeconds(1).toMillis());
    System.out.println("exampleCounter = " + exampleCounter);
  }

  private static void sleep(long millis) {
    Unchecked.run(() -> {
      Thread.sleep(millis);
      return null;
    });
  }

}
