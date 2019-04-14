package com.robertsanek.data.etl.local.sqllite.anki.connect.jsonschema;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cardId",
    "fields",
    "fieldOrder",
    "question",
    "answer",
    "modelName",
    "deckName",
    "css",
    "factor",
    "interval",
    "note"
})
public class CardsInfoResult {

  @JsonProperty("cardId")
  private Long cardId;
  @JsonProperty("fields")
  private Fields fields;
  @JsonProperty("fieldOrder")
  private Long fieldOrder;
  @JsonProperty("question")
  private String question;
  @JsonProperty("answer")
  private String answer;
  @JsonProperty("modelName")
  private String modelName;
  @JsonProperty("deckName")
  private String deckName;
  @JsonProperty("css")
  private String css;
  @JsonProperty("factor")
  private Long factor;
  @JsonProperty("interval")
  private Long interval;
  @JsonProperty("note")
  private Long note;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  @JsonProperty("cardId")
  public Long getCardId() {
    return cardId;
  }

  @JsonProperty("cardId")
  public void setCardId(Long cardId) {
    this.cardId = cardId;
  }

  @JsonProperty("fields")
  public Fields getFields() {
    return fields;
  }

  @JsonProperty("fields")
  public void setFields(Fields fields) {
    this.fields = fields;
  }

  @JsonProperty("fieldOrder")
  public Long getFieldOrder() {
    return fieldOrder;
  }

  @JsonProperty("fieldOrder")
  public void setFieldOrder(Long fieldOrder) {
    this.fieldOrder = fieldOrder;
  }

  @JsonProperty("question")
  public String getQuestion() {
    return question;
  }

  @JsonProperty("question")
  public void setQuestion(String question) {
    this.question = question;
  }

  @JsonProperty("answer")
  public String getAnswer() {
    return answer;
  }

  @JsonProperty("answer")
  public void setAnswer(String answer) {
    this.answer = answer;
  }

  @JsonProperty("modelName")
  public String getModelName() {
    return modelName;
  }

  @JsonProperty("modelName")
  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  @JsonProperty("deckName")
  public String getDeckName() {
    return deckName;
  }

  @JsonProperty("deckName")
  public void setDeckName(String deckName) {
    this.deckName = deckName;
  }

  @JsonProperty("css")
  public String getCss() {
    return css;
  }

  @JsonProperty("css")
  public void setCss(String css) {
    this.css = css;
  }

  @JsonProperty("factor")
  public Long getFactor() {
    return factor;
  }

  @JsonProperty("factor")
  public void setFactor(Long factor) {
    this.factor = factor;
  }

  @JsonProperty("interval")
  public Long getInterval() {
    return interval;
  }

  @JsonProperty("interval")
  public void setInterval(Long interval) {
    this.interval = interval;
  }

  @JsonProperty("note")
  public Long getNote() {
    return note;
  }

  @JsonProperty("note")
  public void setNote(Long note) {
    this.note = note;
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
