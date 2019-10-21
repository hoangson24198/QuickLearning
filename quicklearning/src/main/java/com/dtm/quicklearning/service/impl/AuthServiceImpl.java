package com.dtm.quicklearning.service.impl;

import com.dtm.quicklearning.filter.UserPrincipal;
import com.dtm.quicklearning.model.eNum.RoleName;
import com.dtm.quicklearning.model.entity.PasswordResetToken;
import com.dtm.quicklearning.model.entity.Role;
import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.exception.*;
import com.dtm.quicklearning.model.request.*;
import com.dtm.quicklearning.model.response.JwtAuthenticationResponse;
import com.dtm.quicklearning.model.token.EmailVerificationToken;
import com.dtm.quicklearning.model.token.RefreshToken;
import com.dtm.quicklearning.repository.RoleRepository;
import com.dtm.quicklearning.repository.UserRepository;
import com.dtm.quicklearning.service.*;
import com.dtm.quicklearning.service.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private  AuthenticationManager authenticationManager;

    private  UserRepository userRepository;

    private  RoleRepository roleRepository;

    private  PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider;

    private EmailVerificationTokenService emailVerificationTokenService;

    private final UserService userService;

    private final RefreshTokenService refreshTokenService;

    private final PasswordResetTokenService passwordResetTokenService;


    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider, EmailVerificationTokenService emailVerificationTokenService, UserService userService, RefreshTokenService refreshTokenService, PasswordResetTokenService passwordResetTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider=tokenProvider;
        this.emailVerificationTokenService=emailVerificationTokenService;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public Optional<Authentication> login(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                loginRequest.getPassword())));
    }

    @Override
    public Optional<User> registerUser(SignUpRequest signUpRequest) {
        String newRegistrationRequestEmail = signUpRequest.getEmail();
        if (emailAlreadyExists(newRegistrationRequestEmail)) {
            LOGGER.error("Email already exists: " + newRegistrationRequestEmail);
            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
        }
        LOGGER.info("Trying to register new user [" + newRegistrationRequestEmail + "]");
        User newUser = userService.createUser(signUpRequest);
        User registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    @Override
    public Optional<User> confirmEmailRegistration(String emailToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(emailToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Email verification", emailToken));

        User registeredUser = emailVerificationToken.getUser();
        if (registeredUser.getIsEmailVerified()) {
            LOGGER.info("User [" + emailToken + "] already registered.");
            return Optional.of(registeredUser);
        }

        emailVerificationTokenService.verifyExpiration(emailVerificationToken);
        emailVerificationToken.setConfirmedStatus();
        emailVerificationTokenService.save(emailVerificationToken);

        registeredUser.markVerificationConfirmed();
        userService.save(registeredUser);
        return Optional.of(registeredUser);
    }

    @Override
    public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "Existing email verification", existingToken));

        if (emailVerificationToken.getUser().getIsEmailVerified()) {
            return Optional.empty();
        }
        return Optional.ofNullable(emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));
    }

    @Override
    public Boolean currentPasswordMatches(User currentUser, String password) {
        return passwordEncoder.matches(password, currentUser.getPassWord());
    }

    @Override
    public Optional<User> updatePassword(UserDetailsRequest userDetailsRequest, UpdatePasswordRequest updatePasswordRequest) {
        String email = userDetailsRequest.getEmail();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new UpdatePasswordException(email, "No matching user found"));

        if (!currentPasswordMatches(currentUser, updatePasswordRequest.getOldPassword())) {
            LOGGER.info("Current password is invalid for [" + currentUser.getPassWord() + "]");
            throw new UpdatePasswordException(currentUser.getEmail(), "Invalid current password");
        }
        String newPassword = passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        currentUser.setPassWord(newPassword);
        userService.save(currentUser);
        return Optional.of(currentUser);
    }

    @Override
    public String generateToken(UserDetailsRequest userDetailsRequest) {
        return tokenProvider.generateToken(userDetailsRequest);
    }

    @Override
    public String generateTokenFromUserId(Integer userId) {
        return tokenProvider.generateTokenFromUserId(userId);
    }

    @Override
    public Optional<PasswordResetToken> generatePasswordResetToken(PasswordResetLinkRequest passwordResetLinkRequest) {
        String email = passwordResetLinkRequest.getEmail();
        return userService.findByEmail(email)
                .map(user -> {
                    PasswordResetToken passwordResetToken = passwordResetTokenService.createToken();
                    passwordResetToken.setUser(user);
                    passwordResetTokenService.save(passwordResetToken);
                    return Optional.of(passwordResetToken);
                })
                .orElseThrow(() -> new PasswordResetLinkException(email, "No matching user found for the given request"));
    }

    @Override
    public Optional<User> resetPassword(PasswordResetRequest passwordResetRequest) {
        String token = passwordResetRequest.getToken();
        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Password Reset Token", "Token Id", token));

        passwordResetTokenService.verifyExpiration(passwordResetToken);
        final String encodedPassword = passwordEncoder.encode(passwordResetRequest.getPassword());

        return Optional.of(passwordResetToken)
                .map(PasswordResetToken::getUser)
                .map(user -> {
                    user.setPassWord(encodedPassword);
                    userService.save(user);
                    return user;
                });
    }

    @Override
    public Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginRequest loginRequest) {
        User currentUser = (User) authentication.getPrincipal();
        userRepository.findById(currentUser.getUserId())
                .map(User::getRefreshToken)
                .map(RefreshToken::getId)
                .ifPresent(refreshTokenService::deleteById);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken();
        refreshToken = refreshTokenService.save(refreshToken);
        return Optional.ofNullable(refreshToken);
    }

    @Override
    public boolean emailAlreadyExists(String email) {
        return false;
    }

}
