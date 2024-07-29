package com.ntt.tl.evaluation.controller;

import com.ntt.tl.evaluation.dto.*;
import com.ntt.tl.evaluation.service.IUserServices;
import com.ntt.tl.evaluation.constant.ConstantMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "User", description = "API para la gestión de usuarios")
public class UserController {

	@Autowired
	private IUserServices userServices;

	@Operation(summary = "Crear un nuevo usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseCreateUser.class))),
			@ApiResponse(responseCode = "400", description = ConstantMessage.INVALID_PASSWORD + " o " + ConstantMessage.INVALID_EMAIL),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "409", description = ConstantMessage.EMAIL_EXISTS + " o " + ConstantMessage.ROLE_NOT_EXIST)
	})
	@PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseCreateUser> createUser(@Valid @RequestBody RequestUser userData) {
		return new ResponseEntity<>(userServices.createUser(userData), HttpStatus.CREATED);
	}

	@Operation(summary = "Obtener todos los usuarios")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuarios encontrados",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseListUser.class))),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED)
	})
	@GetMapping(value = "/users")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<ResponseListUser> getAllUser() {
		return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.OK);
	}

	@Operation(summary = "Obtener un usuario por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario encontrado",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER') or hasRole('EDITOR')")
	@GetMapping(value = "/users/{idUser}")
	public ResponseEntity<UserDto> getUser(@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.getOneUser(idUser), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar un usuario por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/users/{idUser}")
	public ResponseEntity<ResponseGeneric> deleteUser(@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.deleteUser(idUser), HttpStatus.OK);
	}

	@Operation(summary = "Actualizar un usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida"),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
	@PutMapping(value = "/users")
	public ResponseEntity<ResponseGeneric> updateUser(@RequestBody RequestUpdateUser userUpdate, BindingResult errors) {
		return new ResponseEntity<>(userServices.updateUser(userUpdate), HttpStatus.OK);
	}
}