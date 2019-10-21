package com.dtm.quicklearning.controller;

import com.dtm.quicklearning.ApiResponse.response.ApiResponse;
import com.dtm.quicklearning.ApiResponse.response.ApiStatus;
import com.dtm.quicklearning.event.OnGenerateResetLinkEvent;
import com.dtm.quicklearning.event.OnUserAccountChangeEvent;
import com.dtm.quicklearning.event.OnUserRegistrationCompleteEvent;
import com.dtm.quicklearning.model.exception.PasswordResetException;
import com.dtm.quicklearning.model.exception.PasswordResetLinkException;
import com.dtm.quicklearning.model.exception.UserLoginException;
import com.dtm.quicklearning.model.exception.UserRegistrationException;
import com.dtm.quicklearning.model.request.*;
import com.dtm.quicklearning.model.response.JwtAuthenticationResponse;
import com.dtm.quicklearning.model.token.RefreshToken;
import com.dtm.quicklearning.service.AuthService;
import com.dtm.quicklearning.service.security.JwtTokenProvider;
import com.dtm.quicklearning.utils.Contants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(Contants.API + Contants.AUTH)
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping(value = "/login")
    public ApiResponse login(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authService.login(loginRequest)
                .orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));
        UserDetailsRequest userDetailsRequest = (UserDetailsRequest) authentication.getPrincipal();
        LOGGER.info("Logged in User returned [API]: " + userDetailsRequest.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authService.createAndPersistRefreshTokenForDevice(authentication, loginRequest)
                .map(RefreshToken::getToken)
                .map(refreshToken -> {
                    String jwtToken = authService.generateToken(userDetailsRequest);
                    return ApiResponse.of(new JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration()));
                })
                .orElseThrow(() -> new UserLoginException("Couldn't create refresh token for: [" + loginRequest + "]"));
    }

    @PostMapping(value = "/register")
    public ApiResponse register(@Valid @RequestBody SignUpRequest signUpRequest){
        return authService.registerUser(signUpRequest)
                .map(user -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/registrationConfirmation");
                    OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent = new OnUserRegistrationCompleteEvent(user, urlBuilder);
                    applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
                    LOGGER.info("Registered User returned [API[: " + user);
                    return ApiResponse.of(ApiStatus.SUCCESS,"User registered successfully. Check your email for verification");
                })
                .orElseThrow(() -> new UserRegistrationException(signUpRequest.getEmail(), "Missing user object in database"));
    }

    @PostMapping(value = "/password/resetlink")
    public ApiResponse resetLink(@Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest){
        return authService.generatePasswordResetToken(passwordResetLinkRequest)
                .map(passwordResetToken -> {
                    UriComponentsBuilder urlBuilder = ServletUriComponentsBuilder.fromCurrentContextPath().path("/password/reset");
                    OnGenerateResetLinkEvent generateResetLinkMailEvent = new OnGenerateResetLinkEvent(passwordResetToken,
                            urlBuilder);
                    applicationEventPublisher.publishEvent(generateResetLinkMailEvent);
                    return ApiResponse.of(ApiStatus.SUCCESS, "Password reset link sent successfully");
                })
                .orElseThrow(() -> new PasswordResetLinkException(passwordResetLinkRequest.getEmail(), "Couldn't create a valid token"));
    }

    @PostMapping("/password/reset")
    @ApiOperation(value = "Reset the password after verification and publish an event to send the acknowledgement " +
            "email")
    public ApiResponse resetPassword(@ApiParam(value = "The PasswordResetRequest payload") @Valid @RequestBody PasswordResetRequest passwordResetRequest) {

        return authService.resetPassword(passwordResetRequest)
                .map(changedUser -> {
                    OnUserAccountChangeEvent onPasswordChangeEvent = new OnUserAccountChangeEvent(changedUser, "Reset Password",
                            "Changed Successfully");
                    applicationEventPublisher.publishEvent(onPasswordChangeEvent);
                    return ApiResponse.of(ApiStatus.SUCCESS, "Password reset link sent successfully");
                })
                .orElseThrow(() -> new PasswordResetException(passwordResetRequest.getToken(), "Error in resetting password"));
    }
}
