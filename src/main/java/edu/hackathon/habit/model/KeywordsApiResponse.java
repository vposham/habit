package edu.hackathon.habit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "code",
        "keywords"
})
public class KeywordsApiResponse {

    @JsonProperty("code")
    private Integer code;
    @JsonProperty("keywords")
    private List<Keyword> keywords = null;

    @JsonProperty("code")
    public Integer getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(Integer code) {
        this.code = code;
    }

    @JsonProperty("keywords")
    public List<Keyword> getKeywords() {
        return keywords;
    }

    @JsonProperty("keywords")
    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

}
