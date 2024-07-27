package com.ntt.tl.evaluation.config.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ntt.tl.evaluation.constant.Constant;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.errors.GenericException;
import com.ntt.tl.evaluation.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UsersEntity userEntity = findUserByEmail(email);
		Collection<? extends GrantedAuthority> authorities = getAuthorities(userEntity);

		return buildUserDetails(userEntity, authorities);
	}

	private UsersEntity findUserByEmail(String email) {
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
	}

	private Collection<? extends GrantedAuthority> getAuthorities(UsersEntity userEntity) {
		return userEntity.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().name()))
				.collect(Collectors.toSet());
	}

	private UserDetails buildUserDetails(UsersEntity userEntity, Collection<? extends GrantedAuthority> authorities) {
		return new User(userEntity.getEmail(), userEntity.getPass(), true, true, true, true, authorities);
	}

}
