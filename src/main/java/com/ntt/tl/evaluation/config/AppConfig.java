package com.ntt.tl.evaluation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class AppConfig {
	
    @Value("${validations.passregex}")
    private String passRegex;
    
    @Value("${validations.emailsregex}")
    private String emailRegex;
    
 
}
