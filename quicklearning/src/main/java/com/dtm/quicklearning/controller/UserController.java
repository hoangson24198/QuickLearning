package com.dtm.quicklearning.controller;

import com.dtm.quicklearning.ApiResponse.response.ApiResponse;
import com.dtm.quicklearning.ApiResponse.response.ApiStatus;
import com.dtm.quicklearning.event.OnUserAccountChangeEvent;
import com.dtm.quicklearning.model.exception.UpdatePasswordException;
import com.dtm.quicklearning.model.request.UpdatePasswordRequest;
import com.dtm.quicklearning.model.request.UserDetailsRequest;
import com.dtm.quicklearning.model.response.UserSummary;
import com.dtm.quicklearning.service.AuthService;
import com.dtm.quicklearning.service.UserService;
import com.dtm.quicklearning.service.security.CurrentUser;
import com.dtm.quicklearning.utils.Contants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(Contants.API+Contants.USER)
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('ROLE_guest','ROLE_student','ROLE_teacher')")
    public UserSummary getCurrentUser(@CurrentUser UserDetailsRequest currentUser){
        LOGGER.info(currentUser.getEmail() + " has role: " + currentUser.getRoles());
        return userService.getCurrentUser(currentUser);
    }

    @GetMapping("/admins")
    @PreAuthorize("hasRole('ROLE_admin')")
    @ApiOperation(value = "Returns the list of configured admins. Requires ADMIN Access")
    public ApiResponse getAllAdmins() {
        LOGGER.info("Inside secured resource with admin");
        return ApiResponse.of(ApiStatus.SUCCESS,"Test role admin");
    }

    @PostMapping("/password/update")
    @PreAuthorize("hasAnyRole('ROLE_guest','ROLE_student','ROLE_teacher')")
    @ApiOperation(value = "Allows the user to change his password once logged in by supplying the correct current " +
            "password")
    public ApiResponse updateUserPassword(@CurrentUser UserDetailsRequest userDetailsRequest,
                                             @ApiParam(value = "The UpdatePasswordRequest payload") @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {

        return authService.updatePassword(userDetailsRequest, updatePasswordRequest)
                .map(updatedUser -> {
                    OnUserAccountChangeEvent onUserPasswordChangeEvent = new OnUserAccountChangeEvent(updatedUser, "Update Password", "Change successful");
                    applicationEventPublisher.publishEvent(onUserPasswordChangeEvent);
                    return ApiResponse.of(ApiStatus.SUCCESS,"Password change successfully");
                })
                .orElseThrow(() -> new UpdatePasswordException("--Empty--", "No such user present."));
    }

    @PostMapping("/logout/{id}")
    public ApiResponse logoutUser(@CurrentUser @PathVariable("id") Integer userId) {
        boolean isLogout = userService.logoutUser(userId);
        if (isLogout){
            return ApiResponse.of(ApiStatus.SUCCESS,"Account logged out!");
        }
        return ApiResponse.error(ApiStatus.NO_CONTENT,"Log out failed!!!");
    }
}
