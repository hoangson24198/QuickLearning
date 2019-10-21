package com.dtm.quicklearning.service;

import com.dtm.quicklearning.model.entity.PasswordResetToken;
import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.request.*;
import com.dtm.quicklearning.model.token.EmailVerificationToken;
import com.dtm.quicklearning.model.token.RefreshToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthService {
    Optional<Authentication> login(LoginRequest loginRequest);

    Optional<User> registerUser(SignUpRequest signUpRequest);

    Optional<User> confirmEmailRegistration(String emailToken);

    Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken);

    Boolean currentPasswordMatches(User currentUser, String password);

    Optional<User> updatePassword(UserDetailsRequest userDetailsRequest,
                                  UpdatePasswordRequest updatePasswordRequest);

    String generateToken(UserDetailsRequest userDetailsRequest);

    String generateTokenFromUserId(Integer userId);

    Optional<PasswordResetToken> generatePasswordResetToken(PasswordResetLinkRequest passwordResetLinkRequest);

    Optional<User> resetPassword(PasswordResetRequest passwordResetRequest);

    Optional<RefreshToken> createAndPersistRefreshTokenForDevice(Authentication authentication, LoginRequest loginRequest);

    boolean emailAlreadyExists(String email);
}
