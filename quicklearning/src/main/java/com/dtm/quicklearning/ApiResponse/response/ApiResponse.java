package com.dtm.quicklearning.ApiResponse.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    @JsonProperty("status")
    private int status;

    @JsonProperty(value = "error")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ApiError error = null;

    @JsonProperty("message")
    private String message;

    @JsonProperty("redirect")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String redirect = "";

    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data = null;

    @JsonProperty("list")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> list = null;


    public ApiResponse(ApiStatus apiStatus, String message, String errorMessage) {
        this.status = apiStatus.getCode();
        this.message = message;

        if (!StringUtils.isEmpty(errorMessage)) {
            this.error = ApiError.of(errorMessage);
        }
    }


    public static ApiResponse of(ApiStatus apiStatus, String message) {
        return new ApiResponse(apiStatus, message, null);
    }

    public static <T> ApiResponse<T> of(T data) {
        ApiResponse response = new ApiResponse(ApiStatus.SUCCESS, null, null);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> of(ApiStatus apiStatus, String message, T data) {
        ApiResponse response = new ApiResponse(apiStatus, message, null);
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<List<T>> of(List<T> data) {
        ApiResponse response = new ApiResponse(ApiStatus.SUCCESS, null, null);
        response.setList(data);
        return response;
    }


    public static <T> ApiResponse<List<T>> of(ApiStatus apiStatus, String message, List<T> data) {
        ApiResponse response = new ApiResponse(apiStatus, message, null);
        response.setList(data);
        return response;
    }

    public static ApiResponse error(ApiStatus apiStatus, String errorMessage) {
        return new ApiResponse(apiStatus, "", errorMessage);
    }

    public static ApiResponse error(ApiStatus apiStatus, String message, String errorMessage) {
        return new ApiResponse(apiStatus, message, errorMessage);
    }

    public static ApiResponse redirect(String redirect) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRedirect(redirect);
        apiResponse.setStatus(ApiStatus.REDIRECT.getCode());
        apiResponse.setMessage("redirect");
        return apiResponse;
    }
}