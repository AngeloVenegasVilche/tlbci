package com.ntt.tl.evaluation.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ntt.tl.evaluation.config.security.TokenProvider;
import com.ntt.tl.evaluation.constant.ERoleUser;
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
import com.ntt.tl.evaluation.dto.RequestUpdateUser;
import com.ntt.tl.evaluation.dto.RequestUser;
import com.ntt.tl.evaluation.dto.ResponseCreateUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.dto.ResponseListUser;
import com.ntt.tl.evaluation.dto.UserDto;
import com.ntt.tl.evaluation.entity.RoleEntity;
import com.ntt.tl.evaluation.entity.UsersEntity;
import com.ntt.tl.evaluation.entity.UsersPhoneEntity;
import com.ntt.tl.evaluation.errors.GenericException;
import com.ntt.tl.evaluation.mapper.UserMapper;
import com.ntt.tl.evaluation.repository.UserRepository;
import com.ntt.tl.evaluation.util.CommonUtil;
import com.ntt.tl.evaluation.util.ErrorUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				email,
				pass
		);

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.createToken(authentication);
		// guardar el token
		return jwt;
	}
	
	/**
	 * Actualiza el ultimo login y token
	 */

	@Override
	public Boolean updateLastLogin(String idUser, String token, Date loginDate) {

		UsersEntity userFind = findValidUser(idUser);

		userFind.setToken(idUser);
		userFind.setLastLogin(loginDate);
		userRepository.save(userFind);

		return true;

	}


	@Override
	public void createAdminUser() {
		Date dateNew = new Date();
		List<String> roleList = Arrays.stream(ERoleUser.values()).map(Enum::name).toList();

		UsersEntity usersEntity = UsersEntity.builder().idUser(CommonUtil.generateUUID())
				.pass(passwordEncoder.encode("Just21.")).name("Administrador").created(dateNew).modified(dateNew)
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

		userRepository.findByEmail(requestUser.getEmail()).ifPresent(user -> {
			throw new GenericException(ConstantMessage.EMAIL_EXISTS, HttpStatus.CONFLICT);
		});

		boolean isValidRol = requestUser.getRoles().stream().allMatch(ErrorUtil::isRoleValid);

		if (!isValidRol) {
			throw new GenericException(ConstantMessage.ROLE_NOT_EXIST, HttpStatus.CONFLICT);
		}

		if (!CommonUtil.validateRegexPattern(requestUser.getPassword(), appConfig.getPassRegex())) {
			throw new GenericException(ConstantMessage.INVALID_PASSWORD, HttpStatus.BAD_REQUEST);
		}

		if (!CommonUtil.validateRegexPattern(requestUser.getEmail(), appConfig.getEmailRegex())) {
			throw new GenericException(ConstantMessage.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
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
	public ResponseGeneric deleteUser(String idUser) {

		UsersEntity userFind = findValidUser(idUser);
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
	public UserDto getOneUser(String idUser) {

		UsersEntity userFind = findValidUser(idUser);
		return userMapper.userBdToUserDto(userFind);
	}

	/**
	 * Actualiza un usuario y sus telefonos.
	 */

	@Override
	public ResponseGeneric updateUser(RequestUpdateUser userRequest) {

		UsersEntity userFind = findValidUser(userRequest.getIdUser());

		userFind.setName(userRequest.getName());
		userFind.setEmail(userRequest.getEmail());
		userFind.setIsActive(userRequest.isActive());
		userRepository.save(userFind);

		return ResponseGeneric.builder().message(ConstantMessage.OK).build();
	}

    /**
     * Encuentra un usuario válido por su ID.
     */
	private UsersEntity findValidUser(String userId) {

		return userRepository.findById(userId)
				.orElseThrow(() -> new GenericException(ConstantMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
	}

}
