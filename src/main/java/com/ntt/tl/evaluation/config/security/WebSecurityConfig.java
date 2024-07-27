package com.ntt.tl.evaluation.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
    public class WebSecurityConfig {
  
		private final JwtAuthEntryPoint unauthorizedHandler;
        private final JwtAuthTokenFilter authenticationJwtTokenFilter;
        
        
        public WebSecurityConfig(JwtAuthEntryPoint unauthorizedHandler,
				JwtAuthTokenFilter authenticationJwtTokenFilter) {
			this.unauthorizedHandler = unauthorizedHandler;
			this.authenticationJwtTokenFilter = authenticationJwtTokenFilter;
		}

    
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    
        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
            return authenticationConfiguration.getAuthenticationManager();
        }
        
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            return http
                    .csrf().disable() // Deshabilitar CSRF
                    .authorizeHttpRequests(req -> req
                        .anyRequest().permitAll()) // Permitir todas las solicitudes
                    .build();
        }
        

        @Bean
        WebSecurityCustomizer webSecurity() {
            
            return w -> w.ignoring()
            		//.requestMatchers(HttpMethod.OPTIONS, "/**")
                    .requestMatchers(HttpMethod.GET, "/health")
                    .requestMatchers(HttpMethod.GET, "/v*/api-docs/**")
                    .requestMatchers(HttpMethod.GET, "/swagger-ui.html")
                    .requestMatchers(HttpMethod.GET, "/swagger-ui/**");

        }
    
    }
