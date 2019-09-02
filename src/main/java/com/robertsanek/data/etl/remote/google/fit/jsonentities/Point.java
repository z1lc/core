
package com.robertsanek.data.etl.remote.google.fit.jsonentities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "startTimeNanos",
    "endTimeNanos",
    "dataTypeName",
    "value",
    "modifiedTimeMillis"
})
public class Point {

    @JsonProperty("startTimeNanos")
    private String startTimeNanos;
    @JsonProperty("endTimeNanos")
    private String endTimeNanos;
    @JsonProperty("dataTypeName")
    private String dataTypeName;
    @JsonProperty("value")
    private List<Value> value = null;
    @JsonProperty("modifiedTimeMillis")
    private String modifiedTimeMillis;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("startTimeNanos")
    public String getStartTimeNanos() {
        return startTimeNanos;
    }

    @JsonProperty("startTimeNanos")
    public void setStartTimeNanos(String startTimeNanos) {
        this.startTimeNanos = startTimeNanos;
    }

    @JsonProperty("endTimeNanos")
    public String getEndTimeNanos() {
        return endTimeNanos;
    }

    @JsonProperty("endTimeNanos")
    public void setEndTimeNanos(String endTimeNanos) {
        this.endTimeNanos = endTimeNanos;
    }

    @JsonProperty("dataTypeName")
    public String getDataTypeName() {
        return dataTypeName;
    }

    @JsonProperty("dataTypeName")
    public void setDataTypeName(String dataTypeName) {
        this.dataTypeName = dataTypeName;
    }

    @JsonProperty("value")
    public List<Value> getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(List<Value> value) {
        this.value = value;
    }

    @JsonProperty("modifiedTimeMillis")
    public String getModifiedTimeMillis() {
        return modifiedTimeMillis;
    }

    @JsonProperty("modifiedTimeMillis")
    public void setModifiedTimeMillis(String modifiedTimeMillis) {
        this.modifiedTimeMillis = modifiedTimeMillis;
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
