package com.ntt.tl.evaluation.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ntt.tl.evaluation.constant.Constant;
import com.ntt.tl.evaluation.dto.RequestPhoneUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.entity.UsersPhoneEntity;
import com.ntt.tl.evaluation.errors.GenericException;
import com.ntt.tl.evaluation.repository.PhoneRepository;
import com.ntt.tl.evaluation.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserPhoneService implements IUserPhoneService {

	@Autowired
	private PhoneRepository phoneRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public ResponseGeneric createPhoneToUser(RequestPhoneUser requestPhoneUser) {

		UsersEntity userFind = userRepository.findById(requestPhoneUser.getIdUser())
				.orElseThrow(() -> new GenericException(Constant.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

		Optional<UsersPhoneEntity> findPhone = phoneRepository.existsByPhoneNumberCityCodeAndCountryCode(
				requestPhoneUser.getPhone().getNumber(), requestPhoneUser.getPhone().getCitycode(),
				requestPhoneUser.getPhone().getContrycode(), requestPhoneUser.getIdUser());

		if (findPhone.isPresent()) {
			throw new GenericException(Constant.PHONE_EXIST_USER, HttpStatus.CONFLICT);
		}

		UsersPhoneEntity usersPhoneEntity = UsersPhoneEntity.builder()
				.cityCode(requestPhoneUser.getPhone().getCitycode())
				.countryCode(requestPhoneUser.getPhone().getContrycode())
				.phoneNumber(requestPhoneUser.getPhone().getNumber()).user(userFind).build();

		usersPhoneEntity = phoneRepository.save(usersPhoneEntity);

		return ResponseGeneric.builder().message("id :" + usersPhoneEntity.getId().toString()).build();

	}

	@Override
	@Transactional
	public ResponseGeneric deletePhoneToUser(String userId, Integer phoneId) {

		UsersEntity usersEntity = findUserValid(userId);
		UsersPhoneEntity findPhone = findPhoneValid(usersEntity, phoneId);

		usersEntity.getPhones().remove(findPhone);

		userRepository.save(usersEntity);
		phoneRepository.delete(findPhone);

		return ResponseGeneric.builder().message(Constant.OK).build();

	}

	@Override
	public ResponseGeneric modifyPhoneToUser(RequestPhoneUser requestPhoneUser) {

		UsersEntity usersEntity = findUserValid(requestPhoneUser.getIdUser());

		UsersPhoneEntity findPhone = findPhoneValid(usersEntity, requestPhoneUser.getPhone().getId());

		findPhone.setCityCode(requestPhoneUser.getPhone().getCitycode());
		findPhone.setCountryCode(requestPhoneUser.getPhone().getContrycode());
		findPhone.setPhoneNumber(requestPhoneUser.getPhone().getNumber());

		phoneRepository.save(findPhone);

		return ResponseGeneric.builder().message(Constant.OK).build();

	}

	private UsersEntity findUserValid(String userId) {

		return userRepository.findById(userId)
				.orElseThrow(() -> new GenericException(Constant.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

	}

	private UsersPhoneEntity findPhoneValid(UsersEntity findUser, Integer phoneId) {

		return findUser.getPhones().stream().filter(o -> o.getId() == phoneId).findFirst()
				.orElseThrow(() -> new GenericException(Constant.PHONE_NOT_EXIST_USER, HttpStatus.NOT_FOUND));

	}

}
