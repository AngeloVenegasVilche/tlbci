package com.ntt.tl.evaluation.mapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ntt.tl.evaluation.constant.ERoleUser;
import org.springframework.stereotype.Component;

import com.ntt.tl.evaluation.dto.PhoneDto;
import com.ntt.tl.evaluation.dto.PhoneUpdateDto;
import com.ntt.tl.evaluation.dto.RequestUser;
import com.ntt.tl.evaluation.dto.ResponseCreateUser;
import com.ntt.tl.evaluation.dto.UserDto;
import com.ntt.tl.evaluation.entity.RoleEntity;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.entity.UsersPhoneEntity;
import com.ntt.tl.evaluation.util.CommonUtil;

@Component
public class UserMapper {

	public List<RoleEntity> listRoleToEntity(List<String> roles) {

		return roles.stream().map(role -> RoleEntity.builder().name(ERoleUser.valueOf(role)).build())
				.collect(Collectors.toList());
	}

	public List<UsersPhoneEntity> listPhoneDtoToUsersListPhoneEntity(List<PhoneDto> list, UsersEntity userSave) {
		List<UsersPhoneEntity> listPhoneEntity = list.stream()
				.map(phone -> UsersPhoneEntity.builder().phoneNumber(phone.getNumber()).user(userSave)
						.cityCode(phone.getCitycode()).countryCode(phone.getContrycode()).build())
				.collect(Collectors.toList());

		return listPhoneEntity;
	}

	public List<PhoneDto> phoneBdToPhoneDto(List<UsersPhoneEntity> phones) {

		List<PhoneDto> listPhoneResponse = phones.stream()
				.map(phoneBd -> PhoneDto.builder().id(phoneBd.getId()).number(phoneBd.getPhoneNumber())
						.citycode(phoneBd.getCityCode()).contrycode(phoneBd.getCountryCode()).build())
				.collect(Collectors.toList());

		return listPhoneResponse;

	}

	public UsersPhoneEntity phoneUpdateDtoToUsersPhoneEntity(PhoneUpdateDto userDto, UsersEntity usersEntity) {

		return UsersPhoneEntity.builder().cityCode(userDto.getCitycode()).countryCode(userDto.getContrycode())
				.phoneNumber(userDto.getNumber()).user(usersEntity).build();
	}

	public UserDto userBdToUserDto(UsersEntity userBd) {

		UserDto userDto = UserDto.builder().idUser(userBd.getIdUser()).name(userBd.getName()).email(userBd.getEmail())
				.lastLogin(userBd.getLastLogin()).modified(userBd.getModified()).created(userBd.getCreated())
				.isActive(userBd.getIsActive()).token(userBd.getToken()).phons(phoneBdToPhoneDto(userBd.getPhones()))
				.build();

		return userDto;

	}

	public List<UserDto> listUsersEntityToListUserDto(List<UsersEntity> listUser) {

		List<UserDto> listUserDto = listUser.stream()
				.map(userBd -> UserDto.builder().idUser(userBd.getIdUser()).name(userBd.getName())
						.email(userBd.getEmail()).lastLogin(userBd.getLastLogin()).modified(userBd.getModified())
						.created(userBd.getCreated()).isActive(userBd.getIsActive()).token(userBd.getToken())
						.phons(phoneBdToPhoneDto(userBd.getPhones())).build())
				.collect(Collectors.toList());

		return listUserDto;
	}

	public ResponseCreateUser usersEntityToResponseCreateUser(UsersEntity userSave) {

		List<String> roles = userSave.getRoles().stream().map(RoleEntity::getName).map(ERoleUser::toString)
				.collect(Collectors.toList());

		ResponseCreateUser responseUser = ResponseCreateUser.builder().idUser(userSave.getIdUser())
				.isActive(userSave.getIsActive()).lastLogin(userSave.getLastLogin()).modified(userSave.getModified())
				.created(userSave.getCreated()).token(userSave.getToken()).roles(roles).build();

		return responseUser;

	}
	
	
	public UsersEntity requestUserToUsersEntity(RequestUser requestUser, String passwordEncoder, String token, Date dateNow, Boolean status) {
		
		UsersEntity userToCreate = UsersEntity.builder()
				.name(requestUser.getName())
				.pass(passwordEncoder)
				.email(requestUser.getEmail())
				.idUser(CommonUtil.generateUUID())
				.token(token)
				.modified(dateNow)
				.created(dateNow)
				.lastLogin(dateNow)
				.isActive(status).build();
		return userToCreate;
	}
	

}
