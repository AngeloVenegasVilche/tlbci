package com.ntt.tl.evaluation.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ntt.tl.evaluation.config.security.TokenProvider;
import com.ntt.tl.evaluation.constant.ERoleUser;
import com.ntt.tl.evaluation.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ntt.tl.evaluation.config.AppConfig;
import com.ntt.tl.evaluation.config.security.JwtUtils;
import com.ntt.tl.evaluation.constant.ConstantMessage;
import com.ntt.tl.evaluation.entity.RoleEntity;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.entity.UsersPhoneEntity;
import com.ntt.tl.evaluation.errors.GenericException;
import com.ntt.tl.evaluation.mapper.UserMapper;
import com.ntt.tl.evaluation.repository.UserRepository;
import com.ntt.tl.evaluation.util.CommonUtil;
import com.ntt.tl.evaluation.util.ErrorUtil;

import jakarta.transaction.Transactional;

/**
 * @author avenegas
 *
 */
@Service
public class UserService implements IUserServices {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AppConfig appConfig;

	@Override
	public String loginUser(String email, String pass) {

		validatePass(pass);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				email,
				pass
		);

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		String jwt = tokenProvider.createToken(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UsersEntity usersEntity = findValidUser(email);

		Date dateNew = new Date();
		usersEntity.setLastLogin(dateNew);
		usersEntity.setToken(jwt);

		userRepository.save(usersEntity);

		return jwt;
	}

	@Override
	public ResponseGeneric activeAccount(RequestActivateAccount requestActivateAccount) {

		UsersEntity findUser = findValidUser(requestActivateAccount.getEmail());
		findUser.setIsActive(requestActivateAccount.getActivate());

		userRepository.save(findUser);

		return ResponseGeneric.builder().message(ConstantMessage.OK).build();
	}

	/**
	 * Actualiza el ultimo login y token
	 */



	@Override
	public void createAdminUser() {
		Date dateNew = new Date();
		List<String> roleList = Arrays.stream(ERoleUser.values()).map(Enum::name).toList();

		UsersEntity usersEntity = UsersEntity.builder().idUser(CommonUtil.generateUUID())
				.pass(passwordEncoder.encode("Just2.")).name("Administrador").created(dateNew).modified(dateNew)
				.email("admin@admin.com").token("").isActive(true).lastLogin(dateNew)
				.roles(roleList.stream().map(role -> RoleEntity.builder().name(ERoleUser.valueOf(role)).build()).toList())
				.build();

		userRepository.save(usersEntity);

	}

	/**
	 * Crea el usuario
	 */
	@Override
	@Transactional
	public ResponseCreateUser createUser(RequestUser requestUser) {

		validateEmail(requestUser.getEmail());
		validatePass(requestUser.getPassword());

		userRepository.findByEmail(requestUser.getEmail()).ifPresent(user -> {
			throw new GenericException(ConstantMessage.EMAIL_EXISTS, HttpStatus.CONFLICT);
		});

		boolean isValidRol = requestUser.getRoles().stream().allMatch(ErrorUtil::isRoleValid);

		if (!isValidRol) {
			throw new GenericException(ConstantMessage.ROLE_NOT_EXIST, HttpStatus.CONFLICT);
		}

		Date dateNew = new Date();
		Boolean inicialStatus = true;
		UsersEntity usersEntity = userMapper.requestUserToUsersEntity(requestUser,
				passwordEncoder.encode(requestUser.getPassword()),
				JwtUtils.getCurrentJwt(),
				dateNew, inicialStatus);

		List<UsersPhoneEntity> listPhone = userMapper.listPhoneDtoToUsersListPhoneEntity(requestUser.getPhones(),
				usersEntity);

		usersEntity.setPhones(listPhone);
		usersEntity.setRoles(userMapper.listRoleToEntity(requestUser.getRoles()));

		UsersEntity userSave = userRepository.save(usersEntity);

		return userMapper.usersEntityToResponseCreateUser(userSave);

	}


	/**
	 * Elimina el usuario.
	 */

	@Override
	public ResponseGeneric deleteUser(String email) {

		UsersEntity userFind = findValidUser(email);
		userRepository.delete(userFind);
		return ResponseGeneric.builder().message(ConstantMessage.OK).build();

	}

	/**
	 * Obtiene todos los usuarios.
	 */

	@Override
	public ResponseListUser getAllUser() {

		List<UsersEntity> listUser = userRepository.findAll();
		List<UserDto> listUserDto = userMapper.listUsersEntityToListUserDto(listUser);

		return (ResponseListUser.builder().userData(listUserDto).build());
	}

	/**
	 * Obtiene un usuario en particular.
	 */

	@Override
	public UserDto getOneUser(String email) {

		UsersEntity userFind = findValidUser(email);
		return userMapper.userBdToUserDto(userFind);
	}

	/**
	 * Actualiza un usuario y sus telefonos.
	 */

	@Override
	public ResponseGeneric updateUser(RequestUpdateUser userRequest) {

		UsersEntity userFind = findValidUser(userRequest.getEmail());

		userFind.setName(userRequest.getName());
		userFind.setPass(passwordEncoder.encode(userRequest.getPass()));
		Date dateNew = new Date();
		userFind.setModified(dateNew);
		userRepository.save(userFind);

		return ResponseGeneric.builder().message(ConstantMessage.OK).build();
	}

	/**
	 * Encuentra un usuario válido por su ID.
	 */
	private UsersEntity findValidUser(String email) {
		validateEmail(email);
		return userRepository.findByEmail(email)
				.orElseThrow(() -> new GenericException(ConstantMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

	private void validatePass(String pass) {
		if (!CommonUtil.validateRegexPattern(pass, appConfig.getPassRegex())) {
			throw new GenericException(ConstantMessage.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
		}

	}
	private void validateEmail (String email){

		if (!CommonUtil.validateRegexPattern(email, appConfig.getEmailRegex())) {
			throw new GenericException(ConstantMessage.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
		}
	}
}
