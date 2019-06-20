package com.robertsanek.data.etl.remote.fitbit.json;

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
    "stages",
    "totalMinutesAsleep",
    "totalSleepRecords",
    "totalTimeInBed"
})
public class Summary {

    @JsonProperty("stages")
    private Stages stages;
    @JsonProperty("totalMinutesAsleep")
    private Long totalMinutesAsleep;
    @JsonProperty("totalSleepRecords")
    private Long totalSleepRecords;
    @JsonProperty("totalTimeInBed")
    private Long totalTimeInBed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("stages")
    public Stages getStages() {
        return stages;
    }

    @JsonProperty("stages")
    public void setStages(Stages stages) {
        this.stages = stages;
    }

    @JsonProperty("totalMinutesAsleep")
    public Long getTotalMinutesAsleep() {
        return totalMinutesAsleep;
    }

    @JsonProperty("totalMinutesAsleep")
    public void setTotalMinutesAsleep(Long totalMinutesAsleep) {
        this.totalMinutesAsleep = totalMinutesAsleep;
    }

    @JsonProperty("totalSleepRecords")
    public Long getTotalSleepRecords() {
        return totalSleepRecords;
    }

    @JsonProperty("totalSleepRecords")
    public void setTotalSleepRecords(Long totalSleepRecords) {
        this.totalSleepRecords = totalSleepRecords;
    }

    @JsonProperty("totalTimeInBed")
    public Long getTotalTimeInBed() {
        return totalTimeInBed;
    }

    @JsonProperty("totalTimeInBed")
    public void setTotalTimeInBed(Long totalTimeInBed) {
        this.totalTimeInBed = totalTimeInBed;
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
