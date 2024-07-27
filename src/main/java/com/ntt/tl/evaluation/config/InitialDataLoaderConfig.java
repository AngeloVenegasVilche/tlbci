package com.ntt.tl.evaluation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ntt.tl.evaluation.service.IUserServices;

@Component
public class InitialDataLoaderConfig implements ApplicationListener<ContextRefreshedEvent> {
	

    @Autowired
    private IUserServices userService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	userService.createAdminUser();
    }
}