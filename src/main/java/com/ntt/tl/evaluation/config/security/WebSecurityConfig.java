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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    
    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
    public class WebSecurityConfig {
  
    	
		private final JwtAuthEntryPoint unauthorizedHandler;
        private final JwtAuthorizationFilter authenticationJwtTokenFilter;
        
        
        public WebSecurityConfig(JwtAuthEntryPoint unauthorizedHandler,
        		JwtAuthorizationFilter authenticationJwtTokenFilter) {
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
            http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                    .authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(authorize -> authorize
                    	.requestMatchers(HttpMethod.POST, "/tl/test/user").permitAll()
                    	.requestMatchers(HttpMethod.GET, "/security/loginUser").permitAll()
                    .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
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
