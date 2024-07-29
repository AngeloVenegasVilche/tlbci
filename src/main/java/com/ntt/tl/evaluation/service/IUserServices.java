package com.ntt.tl.evaluation.service;

import java.util.Date;

import com.ntt.tl.evaluation.dto.*;


public interface IUserServices {

	ResponseCreateUser createUser(RequestUser userData);

	ResponseGeneric deleteUser(String idUser);

	ResponseGeneric activeAccount(RequestActivateAccount requestActivateAccount);

	ResponseListUser getAllUser();

	UserDto getOneUser(String idUser);

	ResponseGeneric updateUser(RequestUpdateUser userUpdate);

	void createAdminUser();

	String loginUser(String email, String pass);

}
