package com.dtm.quicklearning.service;

import com.dtm.quicklearning.filter.UserPrincipal;
import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.request.SignUpRequest;
import com.dtm.quicklearning.model.request.UserDetailsRequest;
import com.dtm.quicklearning.model.response.UserSummary;
import com.dtm.quicklearning.service.security.CurrentUser;

import java.util.Optional;

public interface UserService {
    UserSummary getCurrentUser(UserPrincipal userPrincipal);

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean logoutUser(@CurrentUser Integer id);

    boolean existsByEmail(String email);

    User createUser(SignUpRequest signUpRequest);
    
}
