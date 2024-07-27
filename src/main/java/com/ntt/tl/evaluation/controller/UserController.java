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
@RequestMapping(value = "tl/test")
public class UserController {

	@Autowired
	private IUserServices userServices;
	
    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseCreateUser.class))),
        @ApiResponse(responseCode = "400", description = "Entrada inválida")
    })
	@PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseCreateUser> createUser(@Valid @RequestBody RequestUser userData) {	
		return new ResponseEntity<>(userServices.createUser(userData), HttpStatus.CREATED);
	}

    @Operation(summary = "Obtener todos los usuarios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuarios encontrados",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseListUser.class))),
        @ApiResponse(responseCode = "404", description = "Usuarios no encontrados")
    })
	@GetMapping(value = "/users")
	public ResponseEntity<ResponseListUser> getAllUser() {
		return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.CREATED);
	}

    @Operation(summary = "Obtener un usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
	@GetMapping(value = "/user/{idUser}")
	public ResponseEntity<UserDto> getUser(@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.getOneUser(idUser), HttpStatus.CREATED);
	}

    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseGeneric.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
	@DeleteMapping(value = "/user/{idUser}")
	public ResponseEntity<ResponseGeneric> deleteUser(
			@PathVariable String idUser) {
		return new ResponseEntity<>(userServices.deleteUser(idUser), HttpStatus.CREATED);
	}

    @Operation(summary = "Actualizar un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ResponseGeneric.class))),
        @ApiResponse(responseCode = "400", description = "Entrada inválida"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
	@PutMapping(value = "/user")
	public ResponseEntity<ResponseGeneric> updateUser(
			 @RequestBody RequestUpdateUser userUpdate, 
			BindingResult errors) {
		return new ResponseEntity<>(userServices.updateUser(userUpdate), HttpStatus.CREATED);
	}


}
