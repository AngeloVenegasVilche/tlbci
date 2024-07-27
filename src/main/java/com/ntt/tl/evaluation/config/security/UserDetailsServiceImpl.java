package com.ntt.tl.evaluation.config.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		UsersEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("usuario no encontrado"));
		
		Collection<? extends GrantedAuthority> autoriAuthorities = userEntity.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
				.collect(Collectors.toSet());
		
		return new User(userEntity.getEmail(),
				userEntity.getPass(),
				true,
				true,
				true,
				true,
				autoriAuthorities
				);
	}
	

}
