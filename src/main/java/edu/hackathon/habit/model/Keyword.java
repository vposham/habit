package edu.hackathon.habit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "confidence_score",
        "keyword"
})
public class Keyword {

    @JsonProperty("confidence_score")
    private Double confidenceScore;
    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("confidence_score")
    public Double getConfidenceScore() {
        return confidenceScore;
    }

    @JsonProperty("confidence_score")
    public void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    @JsonProperty("keyword")
    public String getKeyword() {
        return keyword;
    }

    @JsonProperty("keyword")
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}