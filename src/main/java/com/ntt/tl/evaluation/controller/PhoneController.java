package com.ntt.tl.evaluation.controller;

import com.ntt.tl.evaluation.service.IPhoneService;
import com.ntt.tl.evaluation.dto.RequestPhoneUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.constant.ConstantMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "Phone", description = "API para gestionar números telefónicos de usuarios")
public class PhoneController {

	@Autowired
	private IPhoneService userPhoneService;

	@Operation(summary = "Crear un nuevo teléfono para un usuario",
			description = "Crea un nuevo número telefónico para un usuario especificado. Verifica la existencia del usuario y si el número ya está asociado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Teléfono creado exitosamente",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida"),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND),
			@ApiResponse(responseCode = "409", description = ConstantMessage.PHONE_EXIST_USER)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/phones", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGeneric> createUserPhone(@Valid @RequestBody RequestPhoneUser requestPhoneUser) {
		return new ResponseEntity<>(userPhoneService.createPhoneToUser(requestPhoneUser), HttpStatus.CREATED);
	}

	@Operation(summary = "Eliminar un teléfono de un usuario",
			description = "Elimina un número telefónico específico de un usuario. Verifica la existencia tanto del usuario como del teléfono.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND + " o " + ConstantMessage.PHONE_NOT_EXIST_USER)
	})
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/phones/{phoneId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseGeneric> deleteUserPhone(@PathVariable("phoneId") Integer phoneId, @PathVariable("userId") String userId) {
		return new ResponseEntity<>(userPhoneService.deletePhoneToUser(userId, phoneId), HttpStatus.OK);
	}

	@Operation(summary = "Modificar un teléfono de un usuario",
			description = "Modifica un número telefónico existente de un usuario. Verifica la existencia tanto del usuario como del teléfono.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = ConstantMessage.OK,
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
			@ApiResponse(responseCode = "400", description = "Entrada inválida"),
			@ApiResponse(responseCode = "403", description = ConstantMessage.UNAUTHORIZED),
			@ApiResponse(responseCode = "404", description = ConstantMessage.USER_NOT_FOUND + " o " + ConstantMessage.PHONE_NOT_EXIST_USER)
	})
	@PreAuthorize("hasRole('ADMIN') or hasRole('EDITOR')")
	@PutMapping(value = "/phones")
	public ResponseEntity<ResponseGeneric> modifyUserPhone(@Valid @RequestBody RequestPhoneUser requestPhoneUser) {
		return new ResponseEntity<>(userPhoneService.modifyPhoneToUser(requestPhoneUser), HttpStatus.OK);
	}
}