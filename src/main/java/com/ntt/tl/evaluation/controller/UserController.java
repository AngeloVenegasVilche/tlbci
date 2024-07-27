package com.ntt.tl.evaluation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ntt.tl.evaluation.dto.RequestUpdateUser;
import com.ntt.tl.evaluation.dto.RequestUser;
import com.ntt.tl.evaluation.dto.ResponseCreateUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.dto.ResponseListUser;
import com.ntt.tl.evaluation.dto.UserDto;
import com.ntt.tl.evaluation.service.IUserServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


/**
 * @author avenegas
 *
 */
@RestController
//@Api(tags = "Apis de Usuario")
@RequestMapping(value = "especialista/v1")
public class UserController {

	@Autowired
	private IUserServices userServices;
	
	
    @Operation(summary = "Causantes por RUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accepted",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseCreateUser.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content)
    })
	@PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseCreateUser> createUser(@Valid @RequestBody RequestUser userData) {
		
		//ErrorUtil.validateError(errors);
		
		return new ResponseEntity<>(userServices.createUser(userData), HttpStatus.CREATED);
	}

	@GetMapping(value = "/users")
	//@PreAuthorize("hasAnyRole('ADMIN','EDITOR', 'USER')")
	public ResponseEntity<ResponseListUser> getAllUser() {
		return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.CREATED);
	}

	@GetMapping(value = "/user/{idUser}")
	//@PreAuthorize("hasRole('ADMIN','EDITOR', 'USER')")
	public ResponseEntity<UserDto> getUser(@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.getOneUser(idUser), HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/user/{idUser}")
	//@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseGeneric> deleteUser(
			@PathVariable String idUser) {
		
		return new ResponseEntity<>(userServices.deleteUser(idUser), HttpStatus.CREATED);
	}

	@PutMapping(value = "/user")
	//@PreAuthorize("hasRole('ADMIN','EDITOR')")
	public ResponseEntity<ResponseGeneric> updateUser(
			 @RequestBody RequestUpdateUser userUpdate, 
			BindingResult errors) {
		return new ResponseEntity<>(userServices.updateUser(userUpdate), HttpStatus.CREATED);
	}


}
