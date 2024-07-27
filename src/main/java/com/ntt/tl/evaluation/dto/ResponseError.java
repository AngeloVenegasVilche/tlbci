package com.ntt.tl.evaluation.dto;

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
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta para mensajes de error")
public class ResponseError {
	
	@Schema(description = "Mensaje de error", example = "Ha ocurrido un error inesperado")
	private String message;

}
