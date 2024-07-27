package com.ntt.tl.evaluation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author avenegas
 *
 */
@Data
@Builder
@Schema(description = "Detalle cliente")
public class ResponseCreateUser {
	@Schema(description = "Identificador de cliente", example = "456")
	private String idUser;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	private Date created;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	private Date modified;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	private Date lastLogin;
	private String token;
	private Boolean isActive;
	private List<String> roles;

}
