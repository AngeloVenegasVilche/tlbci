package com.ntt.tl.evaluation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RequestPhoneUser {
	
	private String idUser;	// Luego Eliminar.
	private PhoneDto phone;
	

}
