package com.robertsanek.data.etl.local.habitica.jsonentities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonTask {

  @JsonProperty("repeat")
  private Repeat repeat;
  @JsonProperty("challenge")
  private Challenge challenge;
  @JsonProperty("group")
  private Group group;
  @JsonProperty("frequency")
  private String frequency;
  @JsonProperty("everyX")
  private Long everyX;
  @JsonProperty("streak")
  private Long streak;
  @JsonProperty("daysOfMonth")
  private List<Object> daysOfMonth = null;
  @JsonProperty("weeksOfMonth")
  private List<Object> weeksOfMonth = null;
  @JsonProperty("nextDue")
  private List<String> nextDue = null;
  @JsonProperty("yesterDaily")
  private Boolean yesterDaily;
  @JsonProperty("history")
  private List<History> history = null;
  @JsonProperty("completed")
  private Boolean completed;
  @JsonProperty("collapseChecklist")
  private Boolean collapseChecklist;
  @JsonProperty("type")
  private String type;
  @JsonProperty("notes")
  private String notes;
  @JsonProperty("tags")
  private List<Object> tags = null;
  @JsonProperty("value")
  private Double value;
  @JsonProperty("priority")
  private Double priority;
  @JsonProperty("attribute")
  private String attribute;
  @JsonProperty("startDate")
  private String startDate;
  @JsonProperty("checklist")
  private List<ChecklistItem> checklist = null;
  @JsonProperty("reminders")
  private List<Object> reminders = null;
  @JsonProperty("createdAt")
  private String createdAt;
  @JsonProperty("updatedAt")
  private String updatedAt;
  @JsonProperty("text")
  private String text;
  @JsonProperty("userId")
  private String userId;
  @JsonProperty("_legacyId")
  private String legacyId;
  @JsonProperty("isDue")
  private Boolean isDue;
  @JsonProperty("id")
  private String id;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  public double getWeeklyContribution() {
    return getRepeat().getTotalReps() * getTime();
  }

  public double getTime() {
    Matcher matcher = Pattern.compile("^\\d+(\\.\\d+)?$").matcher(notes.split("\n")[0]);
    if (matcher.find()) {
      return Double.parseDouble(matcher.group());
    }
    return getTimeBasedOnPriority();
  }

  public double getTimeBasedOnPriority() {
    if (priority == 0.1) {
      return 5;
    } else if (priority == 1) {
      return 15;
    } else if (priority == 1.5) {
      return 45;
    } else if (priority == 2.0) {
      return 90;
    } else {
      throw new RuntimeException();
    }
  }

  @JsonProperty("repeat")
  public Repeat getRepeat() {
    return repeat;
  }

  @JsonProperty("repeat")
  public void setRepeat(Repeat repeat) {
    this.repeat = repeat;
  }

  @JsonProperty("challenge")
  public Challenge getChallenge() {
    return challenge;
  }

  @JsonProperty("challenge")
  public void setChallenge(Challenge challenge) {
    this.challenge = challenge;
  }

  @JsonProperty("group")
  public Group getGroup() {
    return group;
  }

  @JsonProperty("group")
  public void setGroup(Group group) {
    this.group = group;
  }

  @JsonProperty("frequency")
  public String getFrequency() {
    return frequency;
  }

  @JsonProperty("frequency")
  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  @JsonProperty("everyX")
  public Long getEveryX() {
    return everyX;
  }

  @JsonProperty("everyX")
  public void setEveryX(Long everyX) {
    this.everyX = everyX;
  }

  @JsonProperty("streak")
  public Long getStreak() {
    return streak;
  }

  @JsonProperty("streak")
  public void setStreak(Long streak) {
    this.streak = streak;
  }

  @JsonProperty("daysOfMonth")
  public List<Object> getDaysOfMonth() {
    return daysOfMonth;
  }

  @JsonProperty("daysOfMonth")
  public void setDaysOfMonth(List<Object> daysOfMonth) {
    this.daysOfMonth = daysOfMonth;
  }

  @JsonProperty("weeksOfMonth")
  public List<Object> getWeeksOfMonth() {
    return weeksOfMonth;
  }

  @JsonProperty("weeksOfMonth")
  public void setWeeksOfMonth(List<Object> weeksOfMonth) {
    this.weeksOfMonth = weeksOfMonth;
  }

  @JsonProperty("nextDue")
  public List<String> getNextDue() {
    return nextDue;
  }

  @JsonProperty("nextDue")
  public void setNextDue(List<String> nextDue) {
    this.nextDue = nextDue;
  }

  @JsonProperty("yesterDaily")
  public Boolean getYesterDaily() {
    return yesterDaily;
  }

  @JsonProperty("yesterDaily")
  public void setYesterDaily(Boolean yesterDaily) {
    this.yesterDaily = yesterDaily;
  }

  @JsonProperty("history")
  public List<History> getHistory() {
    return history;
  }

  @JsonProperty("history")
  public void setHistory(List<History> history) {
    this.history = history;
  }

  @JsonProperty("completed")
  public Boolean getCompleted() {
    return completed;
  }

  @JsonProperty("completed")
  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  @JsonProperty("collapseChecklist")
  public Boolean getCollapseChecklist() {
    return collapseChecklist;
  }

  @JsonProperty("collapseChecklist")
  public void setCollapseChecklist(Boolean collapseChecklist) {
    this.collapseChecklist = collapseChecklist;
  }

  @JsonProperty("type")
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  @JsonProperty("notes")
  public String getNotes() {
    return notes;
  }

  @JsonProperty("notes")
  public void setNotes(String notes) {
    this.notes = notes;
  }

  @JsonProperty("tags")
  public List<Object> getTags() {
    return tags;
  }

  @JsonProperty("tags")
  public void setTags(List<Object> tags) {
    this.tags = tags;
  }

  @JsonProperty("value")
  public Double getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(Double value) {
    this.value = value;
  }

  @JsonProperty("priority")
  public Double getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(Double priority) {
    this.priority = priority;
  }

  @JsonProperty("attribute")
  public String getAttribute() {
    return attribute;
  }

  @JsonProperty("attribute")
  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  @JsonProperty("startDate")
  public String getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  @JsonProperty("checklistItem")
  public List<ChecklistItem> getChecklistItem() {
    return checklist;
  }

  @JsonProperty("checklistItem")
  public void setChecklistItem(List<ChecklistItem> checklistItem) {
    this.checklist = checklistItem;
  }

  @JsonProperty("reminders")
  public List<Object> getReminders() {
    return reminders;
  }

  @JsonProperty("reminders")
  public void setReminders(List<Object> reminders) {
    this.reminders = reminders;
  }

  @JsonProperty("createdAt")
  public String getCreatedAt() {
    return createdAt;
  }

  @JsonProperty("createdAt")
  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  @JsonProperty("updatedAt")
  public String getUpdatedAt() {
    return updatedAt;
  }

  @JsonProperty("updatedAt")
  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  @JsonProperty("text")
  public String getText() {
    return text;
  }

  @JsonProperty("text")
  public void setText(String text) {
    this.text = text;
  }

  @JsonProperty("userId")
  public String getUserId() {
    return userId;
  }

  @JsonProperty("userId")
  public void setUserId(String userId) {
    this.userId = userId;
  }

  @JsonProperty("_legacyId")
  public String getLegacyId() {
    return legacyId;
  }

  @JsonProperty("_legacyId")
  public void setLegacyId(String legacyId) {
    this.legacyId = legacyId;
  }

  @JsonProperty("isDue")
  public Boolean getIsDue() {
    return isDue;
  }

  @JsonProperty("isDue")
  public void setIsDue(Boolean isDue) {
    this.isDue = isDue;
  }

  @JsonProperty("id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
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
