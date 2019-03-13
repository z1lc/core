package com.robertsanek.data.etl.local.habitica.jsonentities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group {

  @JsonProperty("approval")
  private Approval approval;
  @JsonProperty("assignedUsers")
  private List<Object> assignedUsers = null;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @JsonProperty("approval")
  public Approval getApproval() {
    return approval;
  }

  @JsonProperty("approval")
  public void setApproval(Approval approval) {
    this.approval = approval;
  }

  @JsonProperty("assignedUsers")
  public List<Object> getAssignedUsers() {
    return assignedUsers;
  }

  @JsonProperty("assignedUsers")
  public void setAssignedUsers(List<Object> assignedUsers) {
    this.assignedUsers = assignedUsers;
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
