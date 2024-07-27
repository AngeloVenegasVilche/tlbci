package com.ntt.tl.evaluation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
@Builder
public class UserDto {
	private String idUser;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	private Date created;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	private Date modified;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	private String name;
	private String email;
	private Date lastLogin;
	private String token;
	private Boolean isActive;
	
	List<PhoneDto> phons;

}
