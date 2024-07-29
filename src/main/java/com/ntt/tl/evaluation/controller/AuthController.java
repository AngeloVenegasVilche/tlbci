package com.ntt.tl.evaluation.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ntt.tl.evaluation.config.security.JWTToken;
import com.ntt.tl.evaluation.config.security.JwtAuthorizationFilter;
import com.ntt.tl.evaluation.dto.LoginDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.service.IUserServices;

@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping(value = "/api")
public class AuthController {
	
	@Autowired
	private IUserServices userServices;
	
	@PostMapping("/login")
    public ResponseEntity<JWTToken> generateToken(@Valid @RequestBody LoginDto login) {

		String jwt = userServices.loginUser(login.getUsername(), login.getPassword());

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add(JwtAuthorizationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
		return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);

    }

}
