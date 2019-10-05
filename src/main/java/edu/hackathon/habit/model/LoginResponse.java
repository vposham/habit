package edu.hackathon.habit.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userId"
})
public class LoginResponse {

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("useruserIdname")
    public void setUserId(String userId) {
        this.userId = userId;
    }


}