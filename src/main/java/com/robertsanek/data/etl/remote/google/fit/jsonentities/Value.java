
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
    "fpVal",
    "mapVal"
})
public class Value {

    @JsonProperty("fpVal")
    private Double fpVal;
    @JsonProperty("mapVal")
    private List<Object> mapVal = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("fpVal")
    public Double getFpVal() {
        return fpVal;
    }

    @JsonProperty("fpVal")
    public void setFpVal(Double fpVal) {
        this.fpVal = fpVal;
    }

    @JsonProperty("mapVal")
    public List<Object> getMapVal() {
        return mapVal;
    }

    @JsonProperty("mapVal")
    public void setMapVal(List<Object> mapVal) {
        this.mapVal = mapVal;
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
