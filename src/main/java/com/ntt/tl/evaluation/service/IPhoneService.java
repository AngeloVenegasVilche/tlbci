package com.ntt.tl.evaluation.service;

import com.ntt.tl.evaluation.dto.RequestPhoneUser;
import com.ntt.tl.evaluation.dto.ResponseGeneric;

public interface IPhoneService {

	ResponseGeneric createPhoneToUser(RequestPhoneUser requestPhoneUser);

	ResponseGeneric deletePhoneToUser(String userId, Integer phoneId);

	ResponseGeneric modifyPhoneToUser(RequestPhoneUser requestPhoneUser);


}
