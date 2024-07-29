package com.ntt.tl.evaluation.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.tl.evaluation.constant.ConstantMessage;
import com.ntt.tl.evaluation.errors.ErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.boot.actuate.web.exchanges.Include.AUTHORIZATION_HEADER;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider tokenProvider;

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String AUTHORITIES_HEADER = "Authorities";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

			String token = resolveToken(request);

			if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {

				Authentication authenticationToken = tokenProvider.getAuthentication(token);

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}

			filterChain.doFilter(request, response);

	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
}

}
