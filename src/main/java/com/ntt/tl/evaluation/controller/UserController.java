package com.ntt.tl.evaluation.controller;

import com.ntt.tl.evaluation.dto.*;
import com.ntt.tl.evaluation.service.IUserServices;
import com.ntt.tl.evaluation.constant.ConstantMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

import java.security.Principal;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "User", description = "API para la gestión de usuarios")
public class UserController {

	@Autowired
	private IUserServices userServices;




	@Operation(summary = "Obtener un usuario en base al contexto")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario encontrado",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('USER')")
	@GetMapping(value = "/users")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
		String nameUser = principal.getName();
		return new ResponseEntity<>(userServices.getOneUser(nameUser), HttpStatus.OK);
	}

	@Operation(summary = "Actualizar un usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida"),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('USER')")
	@PutMapping(value = "/users")
	public ResponseEntity<ResponseGeneric> updateUser(@Valid @RequestBody RequestUpdateUser userUpdate, Principal principal, BindingResult errors) {
		userUpdate.setEmail(principal.getName());
		return new ResponseEntity<>(userServices.updateUser(userUpdate), HttpStatus.OK);
	}

	// ADMIN

	@Operation(summary = "Crear un nuevo usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseCreateUser.class))),
			@ApiResponse(responseCode = "400", description = ConstantMessage.INVALID_PASSWORD + " o " + ConstantMessage.INVALID_EMAIL),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "409", description = ConstantMessage.EMAIL_EXISTS + " o " + ConstantMessage.ROLE_NOT_EXIST)
	})
	@PostMapping(value = "admin/users", produces = MediaType.APPLICATION_JSON_VALUE)
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
	@GetMapping(value = "admin/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ResponseListUser> getAllUser() {
		return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.OK);
	}

	@Operation(summary = "Obtener un usuario por ID solo para administradores")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario encontrado",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "admin/users/{idUser}")
	public ResponseEntity<UserDto> getUser(@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.getOneUser(idUser), HttpStatus.OK);
	}

	@Operation(summary = "Eliminar un usuario por ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuario eliminado",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "404", description = "Usuario no encontrado")
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "admin/users/{idUser}")
	public ResponseEntity<ResponseGeneric> deleteUser(
			@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.deleteUser(idUser), HttpStatus.CREATED);
	}

	@Operation(summary = "Actualizar un usuario")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida"),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "admin/users")
	public ResponseEntity<ResponseGeneric> updateUser(@RequestBody RequestUpdateUser userUpdate) {
		return new ResponseEntity<>(userServices.updateUser(userUpdate), HttpStatus.OK);
	}

	@Operation(summary = "Activación de cuentas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida"),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "admin/activate/users")
	public ResponseEntity<ResponseGeneric> activateAccount(@RequestBody RequestActivateAccount requestActivateAccount ) {
		return new ResponseEntity<>(userServices.activeAccount(requestActivateAccount), HttpStatus.OK);
	}
}