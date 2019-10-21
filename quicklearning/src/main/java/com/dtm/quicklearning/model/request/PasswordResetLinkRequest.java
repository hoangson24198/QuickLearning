package com.dtm.quicklearning.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetLinkRequest {
    @NotBlank(message = "Email cannot be blank")
    @ApiModelProperty(value = "User registered email", required = true, allowableValues = "NonEmpty String")
    private String email;
}
