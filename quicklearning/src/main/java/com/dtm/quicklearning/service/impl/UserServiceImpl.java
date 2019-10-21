package com.dtm.quicklearning.service.impl;

import com.dtm.quicklearning.model.eNum.RoleName;
import com.dtm.quicklearning.model.entity.Role;
import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.exception.AppException;
import com.dtm.quicklearning.model.request.SignUpRequest;
import com.dtm.quicklearning.model.request.UserDetailsRequest;
import com.dtm.quicklearning.model.response.UserSummary;
import com.dtm.quicklearning.repository.RoleRepository;
import com.dtm.quicklearning.repository.UserRepository;
import com.dtm.quicklearning.service.RefreshTokenService;
import com.dtm.quicklearning.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserSummary getCurrentUser(UserDetailsRequest userDetailsRequest) {
        return UserSummary.builder()
                .id(userDetailsRequest.getUserId())
                .email(userDetailsRequest.getEmail())
                .name(userDetailsRequest.getFullName())
                .build();
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public boolean logoutUser(Integer userId) {
        User user = this.userRepository.getOne(userId);
        if (user==null){
            return false;
        }
        LOGGER.info("Removing refresh token associated with device [" + user + "]");
        refreshTokenService.deleteById(user.getRefreshToken().getId());
        return true;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    private Set<Role> getRolesForNewUser(Boolean isToBeMadeAdmin) {
        Set<Role> newUserRoles = new HashSet<>(roleRepository.findAll());
        if (isToBeMadeAdmin) {
            newUserRoles.removeIf(Role::isAdminRole);
        }
        LOGGER.info("Setting user roles: " + newUserRoles);
        return newUserRoles;
    }

    @Override
    public User createUser(SignUpRequest signUpRequest) {
        User newUser = new User();
        Role role = roleRepository.findByName(RoleName.ROLE_guest)
                .orElseThrow(() -> new AppException("User Role not set. Add default roles to database."));
        Boolean isNewUserAsAdmin = signUpRequest.getRegisterAsAdmin();
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setFullName(signUpRequest.getName());
        newUser.setPassWord(passwordEncoder.encode(signUpRequest.getPassword()));
        if (isNewUserAsAdmin) {
            newUser.setRoles(getRolesForNewUser(isNewUserAsAdmin));
        }else {
            newUser.setRoles(Collections.singleton(role));
        }
        newUser.setActive(true);
        newUser.setIsEmailVerified(false);
        return newUser;
    }
}
