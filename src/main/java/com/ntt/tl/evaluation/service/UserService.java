package com.ntt.tl.evaluation.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.ntt.tl.evaluation.config.security.TokenProvider;
import com.ntt.tl.evaluation.constant.ERoleUser;
import com.ntt.tl.evaluation.dto.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class UserService implements IUserServices {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

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

		log.info("Intentando iniciar sesión para el usuario con email: {}", email);

		validatePass(pass);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				email,
				pass
		);

		log.debug("Usuario autenticado exitosamente, generando token.");

		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		String jwt = tokenProvider.createToken(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UsersEntity usersEntity = findValidUser(email);

		Date dateNew = new Date();
		usersEntity.setLastLogin(dateNew);
		usersEntity.setToken(jwt);

		userRepository.save(usersEntity);

		log.info("Inicio de sesión exitoso para el usuario con email: {}", email);
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
		log.info("Creando usuario administrador");
		Date dateNew = new Date();
		List<String> roleList = Arrays.stream(ERoleUser.values()).map(Enum::name).toList();

		UsersEntity usersEntity = UsersEntity.builder().idUser(CommonUtil.generateUUID())
				.pass(passwordEncoder.encode("Just2.")).name("Administrador").created(dateNew).modified(dateNew)
				.email("admin@admin.com").token("").isActive(true).lastLogin(dateNew)
				.roles(roleList.stream().map(role -> RoleEntity.builder().name(ERoleUser.valueOf(role)).build()).toList())
				.build();

		userRepository.save(usersEntity);

		log.info("Usuario administrador creado exitosamente");

	}

	/**
	 * Crea el usuario
	 */
	@Override
	@Transactional
	public ResponseCreateUser createUser(RequestUser requestUser) {
		log.info("Creando usuario con email: {}", requestUser.getEmail());
		validateUniquephone(requestUser.getPhones());
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

		log.info("Usuario creado exitosamente con email: {}", requestUser.getEmail());

		return userMapper.usersEntityToResponseCreateUser(userSave);

	}


	/**
	 * Elimina el usuario.
	 */

	@Override
	public ResponseGeneric deleteUser(String email) {
		log.info("Eliminando usuario con email: {}", email);
		UsersEntity userFind = findValidUser(email);
		userRepository.delete(userFind);
		log.info("Eliminando usuario con exito: {}", email);
		return ResponseGeneric.builder().message(ConstantMessage.OK).build();

	}

	/**
	 * Obtiene todos los usuarios.
	 */

	@Override
	public ResponseListUser getAllUser() {
		log.info("Obteniendo todos los usuario ");
		List<UsersEntity> listUser = userRepository.findAll();
		List<UserDto> listUserDto = userMapper.listUsersEntityToListUserDto(listUser);

		log.info("Obteniendo todos los usuario ok");
		return (ResponseListUser.builder().userData(listUserDto).build());
	}

	/**
	 * Obtiene un usuario en particular.
	 */

	@Override
	public UserDto getOneUser(String email) {
		log.info("Obteniendo un los usuario {}", email);
		UsersEntity userFind = findValidUser(email);
		log.info("Obteniendo un los usuario ok {}", email);
		return userMapper.userBdToUserDto(userFind);
	}

	/**
	 * Actualiza un usuario y sus telefonos.
	 */
	@Override
	public ResponseGeneric updateUser(RequestUpdateUser userRequest) {
		log.info("Actualizando un los usuario {}", userRequest.getEmail());
		UsersEntity userFind = findValidUser(userRequest.getEmail());

		userFind.setName(userRequest.getName());
		userFind.setPass(passwordEncoder.encode(userRequest.getPass()));
		Date dateNew = new Date();
		userFind.setModified(dateNew);
		userRepository.save(userFind);

		log.info("Actualizando un los usuario  ok {}", userRequest.getEmail());
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

	private void validateUniquephone(List<PhoneDto> phones){
		if(phones.stream().distinct().count() != phones.size()){
			throw new GenericException(ConstantMessage.PHONE_UNIQUE, HttpStatus.BAD_REQUEST);
		}
	}
}
