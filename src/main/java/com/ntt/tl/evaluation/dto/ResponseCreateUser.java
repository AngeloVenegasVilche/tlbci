package com.ntt.tl.evaluation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author avenegas
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Detalle del cliente al crear un usuario")
public class ResponseCreateUser {
	@Schema(description = "Identificador de cliente", example = "456")
	private String idUser;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	@Schema(description = "Fecha de creación del usuario", example = "01/01/2024 12:00:00")
	private Date created;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	@Schema(description = "Fecha de última modificación del usuario", example = "01/01/2024 12:00:00")
	private Date modified;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
	@Schema(description = "Fecha de último inicio de sesión del usuario", example = "01/01/2024 12:00:00")
	private Date lastLogin;

	@Schema(description = "Estado activo del usuario", example = "true")
	private Boolean isActive;

	@Schema(description = "Roles del usuario", example = "[\"ADMIN\", \"USER\"]")
	private List<String> roles;

}
