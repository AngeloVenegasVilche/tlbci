package com.ntt.tl.evaluation.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntt.tl.evaluation.constant.ConstantMessage;
import com.ntt.tl.evaluation.errors.ErrorResponse;

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
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path = request.getRequestURI();
		return path.startsWith("/health") ||
				path.startsWith("/v2/api-docs") ||
				path.startsWith("/security/loginUser") ||
				path.startsWith("/v3/api-docs") ||
				path.startsWith("/swagger-ui.html") ||
				path.startsWith("/swagger-ui/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {

			String token = getTokenFromHeader(request.getHeader("Authorization"));

			String userName = jwtUtils.getUserNameForToken(token);

			UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(userName);

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,
					null, userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			filterChain.doFilter(request, response);
			
		} catch (Exception e) {
			handleException(response, HttpStatus.UNAUTHORIZED, ConstantMessage.UNAUTHORIZED);
		}

	}

	private String getTokenFromHeader(String tokenHeader) {
		if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
			throw new UsernameNotFoundException(ConstantMessage.TOKEN_INVALIDO);
		}

		String token = tokenHeader.substring(7);

		if (!jwtUtils.isTokenValid(token)) {
			throw new UsernameNotFoundException(ConstantMessage.TOKEN_INVALIDO);
		}

		return token;
	}

	private void handleException(HttpServletResponse response, HttpStatus status, String message) throws IOException {

		ErrorResponse errorResponse = ErrorResponse.builder().mensaje(message).build();

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);

		response.setStatus(status.value());
		response.setContentType("application/json");
		response.getWriter().write(jsonResponse);
	}
}
