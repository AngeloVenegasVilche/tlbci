package com.ntt.tl.evaluation.config.security;


import java.util.*;
import java.util.stream.Collectors;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.errors.GenericException;
import com.ntt.tl.evaluation.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
* Authenticate a user from the database.
*/
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

private final UserRepository userRepository;

public DomainUserDetailsService(UserRepository userRepository) {

    this.userRepository = userRepository;
}

@Override
@Transactional
public UserDetails loadUserByUsername(final String login) {
    log.debug("Authenticating {}", login);
    return userRepository
        .findByEmail(login)
        .map(user -> createSpringSecurityUser(login, user))
        .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
}

private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, UsersEntity user) {
    if (!user.getIsActive()) {
        throw new GenericException("User " + lowercaseLogin + " was not activated", HttpStatus.UNAUTHORIZED);
    }

    List<GrantedAuthority> grantedAuthorities = user
        .getRoles()
        .stream()
        .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getName().name()))
        .collect(Collectors.toList());
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPass(), grantedAuthorities);
}
}
