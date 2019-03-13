package com.robertsanek.process;

import static j2html.TagCreator.*;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.robertsanek.data.etl.local.habitica.TaskEtl;
import com.robertsanek.data.etl.local.habitica.jsonentities.JsonTask;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;
import com.robertsanek.util.Unchecked;
import com.robertsanek.util.platform.CrossPlatformUtils;

import j2html.tags.ContainerTag;

public class HabiticaLoadAssessment {

  public static final ZonedDateTime now = ZonedDateTime.now();
  private static Log log = Logs.getLog(HabiticaLoadAssessment.class);

  public void generateHtmlSummary() {
    List<JsonTask> allJsonTasks = new TaskEtl().getJsonObjects();
    List<JsonTask> scheduledDailys = allJsonTasks.stream()
        .filter(task -> task.getRepeat().hasSomeRepetition())
        .filter(task -> task.getFrequency().equals("weekly"))
        .sorted(Comparator.comparing(JsonTask::getWeeklyContribution).reversed())
        .collect(Collectors.toList());
    Map<String, Pair<Long, Long>> timePerDay = getTimePerDay(scheduledDailys);

    List<JsonTask> notWeeklyJsonTasks = allJsonTasks.stream()
        .filter(task -> !task.getFrequency().equals("weekly"))
        .collect(Collectors.toList());

    final String regex = "(:[a-zA-Z0-9_]*?:)";

    ContainerTag html = html()
        .with(head()
            .with(meta().attr("charset", "UTF-8"), style().withText(
                "table {border-collapse: collapse;} table, th, td { border: 1px solid black; } span {font-size: 0.7em}")))
        .with(body()
            .with(p("Created at " + now))
            .condWith(notWeeklyJsonTasks.size() > 0,
                p(String.format("%d non-weekly tasks need to be moved to Toodledo: ", notWeeklyJsonTasks.size())))
            .condWith(notWeeklyJsonTasks.size() > 0, ul().with(notWeeklyJsonTasks.stream()
                .map(task -> li(task.getText().replaceAll(regex, "").trim()))
                .collect(Collectors.toList())))
            .with(table().with(
                tr().with(
                    th("JsonTask"),
                    th("Estimated Time"),
                    th("Su"),
                    th("M"),
                    th("Tu"),
                    th("W"),
                    th("Th"),
                    th("F"),
                    th("Sa"),
                    th("Weekly Contribution")
                ))
                .with(scheduledDailys.stream()
                    .map(task -> tr()
                        .attr("style", "background-color: " + (task.getRepeat().isEveryDay() ? "lightgrey" : "white"))
                        .with(td(b(
                            StringUtils.left(task.getText().replaceAll(regex, "").trim(), 50)),
                            br()).with(
                            task.getChecklistItem().stream()
                                .map(item -> span(item.getText()).with(br()))
                                .collect(Collectors.toList())
                        ))
                        .with(td(Long.toString(task.getTimeBasedOnPriority())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getSu())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getM())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getT())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getW())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getTh())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getF())))
                        .with(td(getHtmlBasedOnRepeat(task.getRepeat().getS())))
                        .with(td(Long.toString(task.getWeeklyContribution())))
                    )
                    .collect(Collectors.toList()))
                .with(tr().with(
                    td("Total Individual Tasks").attr("colspan", 2),
                    td(Long.toString(timePerDay.get("Su").getRight())),
                    td(Long.toString(timePerDay.get("M").getRight())),
                    td(Long.toString(timePerDay.get("Tu").getRight())),
                    td(Long.toString(timePerDay.get("W").getRight())),
                    td(Long.toString(timePerDay.get("Th").getRight())),
                    td(Long.toString(timePerDay.get("F").getRight())),
                    td(Long.toString(timePerDay.get("Sa").getRight())))
                )
                .with(tr().with(
                    td("Total Time").attr("colspan", 2),
                    td(Long.toString(timePerDay.get("Su").getLeft())),
                    td(Long.toString(timePerDay.get("M").getLeft())),
                    td(Long.toString(timePerDay.get("Tu").getLeft())),
                    td(Long.toString(timePerDay.get("W").getLeft())),
                    td(Long.toString(timePerDay.get("Th").getLeft())),
                    td(Long.toString(timePerDay.get("F").getLeft())),
                    td(Long.toString(timePerDay.get("Sa").getLeft())))
                )
            )
            .with(p("Dailys not scheduled:"))
            .with(ul().with(
                allJsonTasks.stream()
                    .filter(task -> !task.getRepeat().hasSomeRepetition())
                    .sorted(Comparator.comparing(JsonTask::getWeeklyContribution).reversed())
                    .map(task -> li(task.getText().replaceAll(regex, "").trim()))
                    .collect(Collectors.toList())
            ))
        );
    File loansTarget = new File(CrossPlatformUtils.getRootPathIncludingTrailingSlash().orElseThrow() + "out/habitica.html");
    log.info("Writing to %s", loansTarget.getAbsolutePath());
    try (final PrintWriter writer = Unchecked.get(() -> new PrintWriter(loansTarget, StandardCharsets.UTF_8))) {
      writer.print(html.renderFormatted());
    }
  }

  private Map<String, Pair<Long, Long>> getTimePerDay(List<JsonTask> jsonTasks) {
    return jsonTasks.stream()
        .flatMap(task -> {
          long time = task.getTimeBasedOnPriority();
          return Stream.of(
              Pair.of("Su", task.getRepeat().getSu() ? time : 0),
              Pair.of("M", task.getRepeat().getM() ? time : 0),
              Pair.of("Tu", task.getRepeat().getT() ? time : 0),
              Pair.of("W", task.getRepeat().getW() ? time : 0),
              Pair.of("Th", task.getRepeat().getTh() ? time : 0),
              Pair.of("F", task.getRepeat().getF() ? time : 0),
              Pair.of("Sa", task.getRepeat().getS() ? time : 0)
          );
        })
        .collect(Collectors.groupingBy(Pair::getKey)).entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            entry -> Pair.of(entry.getValue().stream().mapToLong(Pair::getRight).sum(),
                entry.getValue().stream().mapToLong(pair -> pair.getRight() == 0 ? 0 : 1).sum())));
  }

  private String getHtmlBasedOnRepeat(boolean repeat) {
    if (repeat) {
      return "✓";
    } else {
      return "";
    }
  }

}
