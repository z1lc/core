package com.robertsanek.data.etl.remote.scrape.leetcode;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.robertsanek.util.Log;
import com.robertsanek.util.Logs;

@Entity
@Table(name = "leetcode_questions")
public class Question {

  private static Log log = Logs.getLog(Question.class);

  @Id
  private Long id;
  private String title;
  @Enumerated(value = EnumType.STRING)
  private Status status;
  private Long difficulty;

  public enum Status {
    NOT_ATTEMPTED(null),
    ACCEPTED("ac"),
    NOT_ACCEPTED("notac");

    private final String lcName;

    Status(String lcName) {
      this.lcName = lcName;
    }

    public static Status fromValue(String lcName) {
      return Arrays.stream(Status.values())
          .filter(q -> Objects.equal(q.lcName, lcName))
          .findAny()
          .orElseGet(() -> {
            log.error("LeetCode question has String value of %s for status, but no corresponding Status exists.",
                lcName);
            return null;
          });
    }
  }

  public static final class QuestionBuilder {

    private Long id;
    private String title;
    private Status status;
    private Long difficulty;

    private QuestionBuilder() {}

    public static QuestionBuilder aQuestion() {
      return new QuestionBuilder();
    }

    public QuestionBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public QuestionBuilder withTitle(String title) {
      this.title = title;
      return this;
    }

    public QuestionBuilder withStatus(Status status) {
      this.status = status;
      return this;
    }

    public QuestionBuilder withDifficulty(Long difficulty) {
      this.difficulty = difficulty;
      return this;
    }

    public Question build() {
      Question question = new Question();
      question.status = this.status;
      question.title = this.title;
      question.difficulty = this.difficulty;
      question.id = this.id;
      return question;
    }
  }
}
