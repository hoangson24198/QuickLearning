package com.dtm.quicklearning.service.security;

import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.model.exception.NotFoundException;
import com.dtm.quicklearning.model.request.UserDetailsRequest;
import com.dtm.quicklearning.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> dbUser = userRepository.findByEmail(email);
        LOGGER.info("Fetched user : " + dbUser + " by " + email);
        return dbUser.map(UserDetailsRequest::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user email in the database for " + email));
    }

    @Transactional
    public UserDetails loadUserById(Integer id) {
        Optional<User> dbUser = userRepository.findById(id);
        LOGGER.info("Fetched user : " + dbUser + " by " + id);
        return dbUser.map(UserDetailsRequest::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching user id in the database for " + id));
    }
}
