package com.robertsanek.data.etl.remote.oauth.toodledo.jsonentities;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonTask {

  @JsonProperty("id")
  private Integer id;
  @JsonProperty("title")
  private String title;
  @JsonProperty("modified")
  private Instant modified;
  @JsonProperty("completed")
  private Instant completed;
  @JsonProperty("folder")
  private Integer folder;
  @JsonProperty("context")
  private Integer context;
  @JsonProperty("goal")
  private Integer goal;
  @JsonProperty("location")
  private Integer location;
  @JsonProperty("tag")
  private String tag;
  @JsonProperty("remind")
  private Integer remind;
  @JsonProperty("repeat")
  private String repeat;
  @JsonProperty("status")
  private Integer status;
  @JsonProperty("priority")
  private Integer priority;
  @JsonProperty("timer")
  private Integer timer;
  @JsonProperty("timeron")
  private Integer timeron;
  @JsonProperty("note")
  private String note;
  @JsonProperty("children")
  private Integer children;
  @JsonProperty("via")
  private Integer via;
  @JsonProperty("length")
  private Integer length;
  @JsonProperty("repeatfrom")
  private Integer repeatfrom;
  @JsonProperty("star")
  private Integer star;
  @JsonProperty("duedate")
  private Instant duedate;
  @JsonProperty("duedatemod")
  private Integer duedatemod;
  @JsonProperty("startdate")
  private Integer startdate;
  @JsonProperty("added")
  private Instant added;
  @JsonProperty("duetime")
  private Integer duetime;
  @JsonProperty("starttime")
  private Integer starttime;
  @JsonProperty("parent")
  private Integer parent;
  @JsonProperty("order")
  private Integer order;
  @JsonProperty("previous")
  private Integer previous;
  @JsonProperty("addedby")
  private String addedby;
  @JsonProperty("attachment")
  private Integer attachment;
  @JsonProperty("shared")
  private Integer shared;
  @JsonProperty("meta")
  private Object meta;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("id")
  public Integer getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(Integer id) {
    this.id = id;
  }

  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(String title) {
    this.title = title;
  }

  @JsonProperty("modified")
  public Instant getModified() {
    return modified;
  }

  @JsonProperty("modified")
  public void setModified(Instant modified) {
    this.modified = modified;
  }

  @JsonProperty("completed")
  public Instant getCompleted() {
    return completed;
  }

  @JsonProperty("completed")
  public void setCompleted(Instant completed) {
    this.completed = completed;
  }

  @JsonProperty("folder")
  public Integer getFolder() {
    return folder;
  }

  @JsonProperty("folder")
  public void setFolder(Integer folder) {
    this.folder = folder;
  }

  @JsonProperty("context")
  public Integer getContext() {
    return context;
  }

  @JsonProperty("context")
  public void setContext(Integer context) {
    this.context = context;
  }

  @JsonProperty("goal")
  public Integer getGoal() {
    return goal;
  }

  @JsonProperty("goal")
  public void setGoal(Integer goal) {
    this.goal = goal;
  }

  @JsonProperty("location")
  public Integer getLocation() {
    return location;
  }

  @JsonProperty("location")
  public void setLocation(Integer location) {
    this.location = location;
  }

  @JsonProperty("tag")
  public String getTag() {
    return tag;
  }

  @JsonProperty("tag")
  public void setTag(String tag) {
    this.tag = tag;
  }

  @JsonProperty("remind")
  public Integer getRemind() {
    return remind;
  }

  @JsonProperty("remind")
  public void setRemind(Integer remind) {
    this.remind = remind;
  }

  @JsonProperty("repeat")
  public String getRepeat() {
    return repeat;
  }

  @JsonProperty("repeat")
  public void setRepeat(String repeat) {
    this.repeat = repeat;
  }

  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(Integer status) {
    this.status = status;
  }

  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  @JsonProperty("timer")
  public Integer getTimer() {
    return timer;
  }

  @JsonProperty("timer")
  public void setTimer(Integer timer) {
    this.timer = timer;
  }

  @JsonProperty("timeron")
  public Integer getTimeron() {
    return timeron;
  }

  @JsonProperty("timeron")
  public void setTimeron(Integer timeron) {
    this.timeron = timeron;
  }

  @JsonProperty("note")
  public String getNote() {
    return note;
  }

  @JsonProperty("note")
  public void setNote(String note) {
    this.note = note;
  }

  @JsonProperty("children")
  public Integer getChildren() {
    return children;
  }

  @JsonProperty("children")
  public void setChildren(Integer children) {
    this.children = children;
  }

  @JsonProperty("via")
  public Integer getVia() {
    return via;
  }

  @JsonProperty("via")
  public void setVia(Integer via) {
    this.via = via;
  }

  @JsonProperty("length")
  public Integer getLength() {
    return length;
  }

  @JsonProperty("length")
  public void setLength(Integer length) {
    this.length = length;
  }

  @JsonProperty("repeatfrom")
  public Integer getRepeatfrom() {
    return repeatfrom;
  }

  @JsonProperty("repeatfrom")
  public void setRepeatfrom(Integer repeatfrom) {
    this.repeatfrom = repeatfrom;
  }

  @JsonProperty("star")
  public Integer getStar() {
    return star;
  }

  @JsonProperty("star")
  public void setStar(Integer star) {
    this.star = star;
  }

  @JsonProperty("duedate")
  public Instant getDuedate() {
    return duedate;
  }

  @JsonProperty("duedate")
  public void setDuedate(Instant duedate) {
    this.duedate = duedate;
  }

  @JsonProperty("duedatemod")
  public Integer getDuedatemod() {
    return duedatemod;
  }

  @JsonProperty("duedatemod")
  public void setDuedatemod(Integer duedatemod) {
    this.duedatemod = duedatemod;
  }

  @JsonProperty("startdate")
  public Integer getStartdate() {
    return startdate;
  }

  @JsonProperty("startdate")
  public void setStartdate(Integer startdate) {
    this.startdate = startdate;
  }

  @JsonProperty("added")
  public Instant getAdded() {
    return added;
  }

  @JsonProperty("added")
  public void setAdded(Instant added) {
    this.added = added;
  }

  @JsonProperty("duetime")
  public Integer getDuetime() {
    return duetime;
  }

  @JsonProperty("duetime")
  public void setDuetime(Integer duetime) {
    this.duetime = duetime;
  }

  @JsonProperty("starttime")
  public Integer getStarttime() {
    return starttime;
  }

  @JsonProperty("starttime")
  public void setStarttime(Integer starttime) {
    this.starttime = starttime;
  }

  @JsonProperty("parent")
  public Integer getParent() {
    return parent;
  }

  @JsonProperty("parent")
  public void setParent(Integer parent) {
    this.parent = parent;
  }

  @JsonProperty("order")
  public Integer getOrder() {
    return order;
  }

  @JsonProperty("order")
  public void setOrder(Integer order) {
    this.order = order;
  }

  @JsonProperty("previous")
  public Integer getPrevious() {
    return previous;
  }

  @JsonProperty("previous")
  public void setPrevious(Integer previous) {
    this.previous = previous;
  }

  @JsonProperty("addedby")
  public String getAddedby() {
    return addedby;
  }

  @JsonProperty("addedby")
  public void setAddedby(String addedby) {
    this.addedby = addedby;
  }

  @JsonProperty("attachment")
  public Integer getAttachment() {
    return attachment;
  }

  @JsonProperty("attachment")
  public void setAttachment(Integer attachment) {
    this.attachment = attachment;
  }

  @JsonProperty("shared")
  public Integer getShared() {
    return shared;
  }

  @JsonProperty("shared")
  public void setShared(Integer shared) {
    this.shared = shared;
  }

  @JsonProperty("meta")
  public Object getMeta() {
    return meta;
  }

  @JsonProperty("meta")
  public void setMeta(Object meta) {
    this.meta = meta;
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