package com.ntt.tl.evaluation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntt.tl.evaluation.dto.RequestPhoneUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.service.IUserPhoneService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "especialista/v1")
public class PhoneController {
	
	@Autowired
	private IUserPhoneService userPhoneService;
	
	
	@PostMapping(value = "/phones", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGeneric> createUserPhone(@Valid @RequestBody RequestPhoneUser requestPhoneUser) {
				
		return new ResponseEntity<>(userPhoneService.createPhoneToUser(requestPhoneUser), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/phones/{phoneId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGeneric> deleteUserPhone(@PathVariable("phoneId") Integer phoneId, @PathVariable("userId") String userId) {
		return new ResponseEntity<>(userPhoneService.deletePhoneToUser(userId, phoneId), HttpStatus.OK);
	}
	
	
	@PutMapping(value = "/phones")
	public ResponseEntity<ResponseGeneric> modifyUserPhone(@Valid @RequestBody RequestPhoneUser requestPhoneUser) {
		return new ResponseEntity<>(userPhoneService.modifyPhoneToUser(requestPhoneUser), HttpStatus.OK);
	}
	

}
