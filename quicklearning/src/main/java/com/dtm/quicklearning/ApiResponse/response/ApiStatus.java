package com.dtm.quicklearning.ApiResponse.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ApiStatus implements CodeEnum {
    SUCCESS(0),
    NO_CONTENT(204),
    BAD_REQUEST(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    NOT_FOUND_USER(-10),
    METHOD_NOT_ALLOWED(405),
    REDIRECT(302),
    SYSTEM_ERROR(500);

    private final int code;

    ApiStatus(int code) {
        this.code = code;
    }

    public static ApiStatus getApiStatus(int code) {
        ApiStatus[] apiStatuses = ApiStatus.values();
        for (ApiStatus apiStatus : apiStatuses) {
            if (code == apiStatus.getCode()) {
                return apiStatus;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public int getCode() {
        return this.code;
    }

}