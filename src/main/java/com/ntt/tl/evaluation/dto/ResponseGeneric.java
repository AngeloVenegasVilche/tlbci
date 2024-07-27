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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Objeto de respuesta genérica")
public class ResponseGeneric {
	@Schema(description = "Mensaje de respuesta", example = "ok")
	private String message;

}
