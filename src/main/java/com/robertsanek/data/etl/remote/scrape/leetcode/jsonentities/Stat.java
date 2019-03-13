package com.robertsanek.data.etl.remote.scrape.leetcode.jsonentities;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "question_id",
    "question__article__live",
    "question__article__slug",
    "question__title",
    "question__title_slug",
    "question__hide",
    "total_acs",
    "total_submitted",
    "frontend_question_id",
    "is_new_question"
})
public class Stat {

  @JsonProperty("question_id")
  private Integer questionId;
  @JsonProperty("question__article__live")
  private Boolean questionArticleLive;
  @JsonProperty("question__article__slug")
  private String questionArticleSlug;
  @JsonProperty("question__title")
  private String questionTitle;
  @JsonProperty("question__title_slug")
  private String questionTitleSlug;
  @JsonProperty("question__hide")
  private Boolean questionHide;
  @JsonProperty("total_acs")
  private Integer totalAcs;
  @JsonProperty("total_submitted")
  private Integer totalSubmitted;
  @JsonProperty("frontend_question_id")
  private Integer frontendQuestionId;
  @JsonProperty("is_new_question")
  private Boolean isNewQuestion;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("question_id")
  public Integer getQuestionId() {
    return questionId;
  }

  @JsonProperty("question_id")
  public void setQuestionId(Integer questionId) {
    this.questionId = questionId;
  }

  @JsonProperty("question__article__live")
  public Boolean getQuestionArticleLive() {
    return questionArticleLive;
  }

  @JsonProperty("question__article__live")
  public void setQuestionArticleLive(Boolean questionArticleLive) {
    this.questionArticleLive = questionArticleLive;
  }

  @JsonProperty("question__article__slug")
  public String getQuestionArticleSlug() {
    return questionArticleSlug;
  }

  @JsonProperty("question__article__slug")
  public void setQuestionArticleSlug(String questionArticleSlug) {
    this.questionArticleSlug = questionArticleSlug;
  }

  @JsonProperty("question__title")
  public String getQuestionTitle() {
    return questionTitle;
  }

  @JsonProperty("question__title")
  public void setQuestionTitle(String questionTitle) {
    this.questionTitle = questionTitle;
  }

  @JsonProperty("question__title_slug")
  public String getQuestionTitleSlug() {
    return questionTitleSlug;
  }

  @JsonProperty("question__title_slug")
  public void setQuestionTitleSlug(String questionTitleSlug) {
    this.questionTitleSlug = questionTitleSlug;
  }

  @JsonProperty("question__hide")
  public Boolean getQuestionHide() {
    return questionHide;
  }

  @JsonProperty("question__hide")
  public void setQuestionHide(Boolean questionHide) {
    this.questionHide = questionHide;
  }

  @JsonProperty("total_acs")
  public Integer getTotalAcs() {
    return totalAcs;
  }

  @JsonProperty("total_acs")
  public void setTotalAcs(Integer totalAcs) {
    this.totalAcs = totalAcs;
  }

  @JsonProperty("total_submitted")
  public Integer getTotalSubmitted() {
    return totalSubmitted;
  }

  @JsonProperty("total_submitted")
  public void setTotalSubmitted(Integer totalSubmitted) {
    this.totalSubmitted = totalSubmitted;
  }

  @JsonProperty("frontend_question_id")
  public Integer getFrontendQuestionId() {
    return frontendQuestionId;
  }

  @JsonProperty("frontend_question_id")
  public void setFrontendQuestionId(Integer frontendQuestionId) {
    this.frontendQuestionId = frontendQuestionId;
  }

  @JsonProperty("is_new_question")
  public Boolean getIsNewQuestion() {
    return isNewQuestion;
  }

  @JsonProperty("is_new_question")
  public void setIsNewQuestion(Boolean isNewQuestion) {
    this.isNewQuestion = isNewQuestion;
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

}
