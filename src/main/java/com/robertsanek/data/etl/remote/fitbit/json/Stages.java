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
    "deep",
    "light",
    "rem",
    "wake"
})
public class Stages {

    @JsonProperty("deep")
    private Long deep;
    @JsonProperty("light")
    private Long light;
    @JsonProperty("rem")
    private Long rem;
    @JsonProperty("wake")
    private Long wake;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("deep")
    public Long getDeep() {
        return deep;
    }

    @JsonProperty("deep")
    public void setDeep(Long deep) {
        this.deep = deep;
    }

    @JsonProperty("light")
    public Long getLight() {
        return light;
    }

    @JsonProperty("light")
    public void setLight(Long light) {
        this.light = light;
    }

    @JsonProperty("rem")
    public Long getRem() {
        return rem;
    }

    @JsonProperty("rem")
    public void setRem(Long rem) {
        this.rem = rem;
    }

    @JsonProperty("wake")
    public Long getWake() {
        return wake;
    }

    @JsonProperty("wake")
    public void setWake(Long wake) {
        this.wake = wake;
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
