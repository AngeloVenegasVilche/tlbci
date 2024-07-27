package com.ntt.tl.evaluation.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ntt.tl.evaluation.constant.Constant;
import com.ntt.tl.evaluation.errors.GenericException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {


		String token = getTokenFromHeader(request.getHeader("Authorization"));

		String userName = jwtUtils.getUserNameForToken(token);
		
		UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);
		
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,
				null, userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		filterChain.doFilter(request, response);
	}
	
    private String getTokenFromHeader(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            throw new GenericException(Constant.TOKEN_INVALIDO, HttpStatus.UNAUTHORIZED);
        }
        
        String token = tokenHeader.substring(7);
        
        if (!jwtUtils.isTokenValid(token)) {
        	throw new GenericException(Constant.TOKEN_INVALIDO, HttpStatus.UNAUTHORIZED);
        }
         
        return token;
    }
}
