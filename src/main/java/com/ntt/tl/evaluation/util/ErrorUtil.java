package com.ntt.tl.evaluation.util;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.ntt.tl.evaluation.constant.ERole;
import com.ntt.tl.evaluation.errors.GenericException;

/**
 * @author avenegas
 *
 */
public class ErrorUtil {

	   

	/**
	 * @param requestUser
	 * @param errors
	 */
	public static void validateError(BindingResult errors) {
		String errorsDetail = ErrorUtil.getDetailError(errors);

		if (!errorsDetail.isEmpty()) {
			throw new GenericException(errorsDetail, HttpStatus.CONFLICT);
		}
	}

	/**
	 * @param errors
	 * @return
	 */
	public static final String getDetailError(BindingResult errors) {

		String errorDetail = "";

		if (errors.hasErrors()) {
			for (FieldError error : errors.getFieldErrors()) {
				String fieldName = error.getField();
				String errorMessage = error.getDefaultMessage();

				errorDetail = errorDetail + fieldName + "|" + errorMessage + " ";
			}

		}

		return errorDetail;
	}

	public static boolean isRoleValid(String roleName) {
		try {
			ERole.valueOf(roleName);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}

	}
}
