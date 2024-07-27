package com.ntt.tl.evaluation.service;

import java.util.Date;

import com.ntt.tl.evaluation.dto.RequestUpdateUser;
import com.ntt.tl.evaluation.dto.RequestUser;
import com.ntt.tl.evaluation.dto.ResponseCreateUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;
import com.ntt.tl.evaluation.dto.ResponseListUser;
import com.ntt.tl.evaluation.dto.UserDto;


public interface IUserServices {

	ResponseCreateUser createUser(RequestUser userData);

	ResponseGeneric deleteUser(String idUser);

	Boolean updateLastLogin(String idUser, String token, Date loginDate);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate);

	void createAdminUser();

	ResponseGeneric loginUser(String email, String pass);

}
