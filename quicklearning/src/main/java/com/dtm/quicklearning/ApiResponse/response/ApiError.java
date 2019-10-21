package com.dtm.quicklearning.ApiResponse.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class ApiError {

    @JsonProperty("message")
    @NonNull
    private String message;

    @JsonProperty("requiredKey")
    private String requiredKey;
}