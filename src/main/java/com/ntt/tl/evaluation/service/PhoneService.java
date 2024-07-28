package com.ntt.tl.evaluation.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ntt.tl.evaluation.constant.ConstantMessage;
import com.ntt.tl.evaluation.dto.RequestPhoneUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.entity.UsersPhoneEntity;
import com.ntt.tl.evaluation.errors.GenericException;
import com.ntt.tl.evaluation.repository.PhoneRepository;
import com.ntt.tl.evaluation.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;

@Service
public class PhoneService implements IPhoneService {

	@Autowired
	private PhoneRepository phoneRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
    @Operation(summary = "Crear un teléfono para un usuario", description = "Añade un teléfono a un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teléfono creado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "El teléfono ya existe para el usuario", content = @Content)
    })
	public ResponseGeneric createPhoneToUser(RequestPhoneUser requestPhoneUser) {

		UsersEntity userFind = userRepository.findById(requestPhoneUser.getIdUser())
				.orElseThrow(() -> new GenericException(ConstantMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		Optional<UsersPhoneEntity> findPhone = phoneRepository.existsByPhoneNumberCityCodeAndCountryCode(
				requestPhoneUser.getPhone().getNumber(), requestPhoneUser.getPhone().getCitycode(),
				requestPhoneUser.getPhone().getContrycode(), requestPhoneUser.getIdUser());

		if (findPhone.isPresent()) {
			throw new GenericException(ConstantMessage.PHONE_EXIST_USER, HttpStatus.CONFLICT);
		}

		UsersPhoneEntity usersPhoneEntity = UsersPhoneEntity.builder()
				.cityCode(requestPhoneUser.getPhone().getCitycode())
				.countryCode(requestPhoneUser.getPhone().getContrycode())
				.phoneNumber(requestPhoneUser.getPhone().getNumber()).user(userFind).build();

		usersPhoneEntity = phoneRepository.save(usersPhoneEntity);

		return ResponseGeneric.builder().message("id :" + usersPhoneEntity.getId().toString()).build();

	}

	@Override
    @Operation(summary = "Eliminar un teléfono de un usuario", description = "Elimina un teléfono de un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teléfono eliminado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
        @ApiResponse(responseCode = "404", description = "Usuario o teléfono no encontrado", content = @Content)
    })
	@Transactional
	public ResponseGeneric deletePhoneToUser(String userId, Integer phoneId) {

		UsersEntity usersEntity = findUserValid(userId);
		UsersPhoneEntity findPhone = findPhoneValid(usersEntity, phoneId);

		usersEntity.getPhones().remove(findPhone);

		userRepository.save(usersEntity);
		phoneRepository.delete(findPhone);

		return ResponseGeneric.builder().message(ConstantMessage.OK).build();

	}

	@Override
    @Operation(summary = "Modificar un teléfono de un usuario", description = "Modifica un teléfono de un usuario existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Teléfono modificado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseGeneric.class))),
        @ApiResponse(responseCode = "404", description = "Usuario o teléfono no encontrado", content = @Content)
    })
	public ResponseGeneric modifyPhoneToUser(RequestPhoneUser requestPhoneUser) {

		UsersEntity usersEntity = findUserValid(requestPhoneUser.getIdUser());

		UsersPhoneEntity findPhone = findPhoneValid(usersEntity, requestPhoneUser.getPhone().getId());

		findPhone.setCityCode(requestPhoneUser.getPhone().getCitycode());
		findPhone.setCountryCode(requestPhoneUser.getPhone().getContrycode());
		findPhone.setPhoneNumber(requestPhoneUser.getPhone().getNumber());

		phoneRepository.save(findPhone);

		return ResponseGeneric.builder().message(ConstantMessage.OK).build();

	}

	private UsersEntity findUserValid(String userId) {

		return userRepository.findById(userId)
				.orElseThrow(() -> new GenericException(ConstantMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

	}

	private UsersPhoneEntity findPhoneValid(UsersEntity findUser, Integer phoneId) {

		return findUser.getPhones().stream().filter(o -> o.getId() == phoneId).findFirst()
				.orElseThrow(() -> new GenericException(ConstantMessage.PHONE_NOT_EXIST_USER, HttpStatus.NOT_FOUND));

	}

}
