package com.ntt.tl.evaluation.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Schema(description = "Objeto de respuesta para la lista de usuarios")
public class ResponseListUser {
	
	@ArraySchema(schema = @Schema(description = "Lista de datos de los usuarios", implementation = UserDto.class))
	private List<UserDto> userData;

}
