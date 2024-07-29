package com.ntt.tl.evaluation.service;

import java.util.Optional;

import com.ntt.tl.evaluation.config.AppConfig;
import com.ntt.tl.evaluation.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PhoneService implements IPhoneService {

	@Autowired
	private PhoneRepository phoneRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AppConfig appConfig;

	@Override
	public ResponseGeneric createPhoneToUser(RequestPhoneUser requestPhoneUser) {
		log.info("Inicio de createPhoneToUser con email: {}", requestPhoneUser.getEmaial());
		UsersEntity userFind = findUserValid(requestPhoneUser.getEmaial());

		Optional<UsersPhoneEntity> findPhone = phoneRepository.existsByPhoneNumberCityCodeAndCountryCode(
				requestPhoneUser.getPhone().getNumber(), requestPhoneUser.getPhone().getCitycode(),
				requestPhoneUser.getPhone().getContrycode(), requestPhoneUser.getEmaial());

		if (findPhone.isPresent()) {
			throw new GenericException(ConstantMessage.PHONE_EXIST_USER, HttpStatus.CONFLICT);
		}

		UsersPhoneEntity usersPhoneEntity = UsersPhoneEntity.builder()
				.cityCode(requestPhoneUser.getPhone().getCitycode())
				.countryCode(requestPhoneUser.getPhone().getContrycode())
				.phoneNumber(requestPhoneUser.getPhone().getNumber()).user(userFind).build();

		usersPhoneEntity = phoneRepository.save(usersPhoneEntity);
		log.info("Teléfono guardado con ID: {}", usersPhoneEntity.getId());
		return ResponseGeneric.builder().message("id :" + usersPhoneEntity.getId().toString()).build();

	}

	@Override
	@Transactional
	public ResponseGeneric deletePhoneToUser(String userId, Integer phoneId) {
		log.info("Inicio de deletePhoneToUser con userId: {}, phoneId: {}", userId, phoneId);
		UsersEntity usersEntity = findUserValid(userId);
		UsersPhoneEntity findPhone = findPhoneValid(usersEntity, phoneId);

		usersEntity.getPhones().remove(findPhone);

		userRepository.save(usersEntity);
		phoneRepository.delete(findPhone);

		log.info("Teléfono con ID: {} eliminado para el usuario: {}", phoneId, userId);
		return ResponseGeneric.builder().message(ConstantMessage.OK).build();

	}

	@Override
	public ResponseGeneric modifyPhoneToUser(RequestPhoneUser requestPhoneUser) {
		log.info("Inicio de modifyPhoneToUser con email: {}", requestPhoneUser.getEmaial());
		UsersEntity usersEntity = findUserValid(requestPhoneUser.getEmaial());

		UsersPhoneEntity findPhone = findPhoneValid(usersEntity, requestPhoneUser.getPhone().getId());

		findPhone.setCityCode(requestPhoneUser.getPhone().getCitycode());
		findPhone.setCountryCode(requestPhoneUser.getPhone().getContrycode());
		findPhone.setPhoneNumber(requestPhoneUser.getPhone().getNumber());

		phoneRepository.save(findPhone);
		log.info("Teléfono con ID: {} modificado para el usuario: {}", findPhone.getId(), requestPhoneUser.getEmaial());

		return ResponseGeneric.builder().message(ConstantMessage.OK).build();

	}

	private UsersEntity findUserValid(String email) {
		log.info("Validando usuario con email: {}", email);
		if (!CommonUtil.validateRegexPattern(email, appConfig.getEmailRegex())) {
			throw new GenericException(ConstantMessage.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
		}

		UsersEntity user = userRepository.findByEmail(email)
				.orElseThrow(() -> new GenericException(ConstantMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
		log.info("Usuario encontrado con email: {}", email);
		return user;
	}

	private UsersPhoneEntity findPhoneValid(UsersEntity findUser, Integer phoneId) {
		log.info("Validando teléfono con ID: {} para el usuario con email: {}", phoneId, findUser.getEmail());
		UsersPhoneEntity phone = findUser.getPhones().stream()
				.filter(o -> o.getId() == phoneId)
				.findFirst()
				.orElseThrow(() -> new GenericException(ConstantMessage.PHONE_NOT_EXIST_USER, HttpStatus.NOT_FOUND));
		log.info("Teléfono válido encontrado con ID: {}", phoneId);
		return phone;
	}

}
