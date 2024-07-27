package com.ntt.tl.evaluation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.service.IUserServices;

@RestController
public class AuthController {
	
	@Autowired
	private IUserServices userServices;
	
	@GetMapping("/security/loginUser")
    public ResponseEntity<ResponseGeneric> generateToken(@RequestParam("email") String username, @RequestParam("pass") String pass) {
			
		return new ResponseEntity<>(userServices.loginUser(username, pass), HttpStatus.CREATED);

    }
	
}
