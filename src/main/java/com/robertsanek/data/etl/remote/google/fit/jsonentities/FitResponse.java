
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
    "minStartTimeNs",
    "maxEndTimeNs",
    "dataSourceId",
    "point"
})
public class FitResponse {

    @JsonProperty("minStartTimeNs")
    private String minStartTimeNs;
    @JsonProperty("maxEndTimeNs")
    private String maxEndTimeNs;
    @JsonProperty("dataSourceId")
    private String dataSourceId;
    @JsonProperty("point")
    private List<Point> point = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("minStartTimeNs")
    public String getMinStartTimeNs() {
        return minStartTimeNs;
    }

    @JsonProperty("minStartTimeNs")
    public void setMinStartTimeNs(String minStartTimeNs) {
        this.minStartTimeNs = minStartTimeNs;
    }

    @JsonProperty("maxEndTimeNs")
    public String getMaxEndTimeNs() {
        return maxEndTimeNs;
    }

    @JsonProperty("maxEndTimeNs")
    public void setMaxEndTimeNs(String maxEndTimeNs) {
        this.maxEndTimeNs = maxEndTimeNs;
    }

    @JsonProperty("dataSourceId")
    public String getDataSourceId() {
        return dataSourceId;
    }

    @JsonProperty("dataSourceId")
    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    @JsonProperty("point")
    public List<Point> getPoint() {
        return point;
    }

    @JsonProperty("point")
    public void setPoint(List<Point> point) {
        this.point = point;
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
