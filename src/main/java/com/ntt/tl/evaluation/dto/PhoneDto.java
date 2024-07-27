package com.ntt.tl.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author avenegas
 *
 */
@Data
@Valid
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de teléfono del usuario")
public class PhoneDto {
	
	@Schema(description = "ID del teléfono", example = "1")
	private Integer id;
	
	@NotEmpty
	@Pattern(regexp = "\\d+", message = "Debe contener solo números")
	@Size(min = 9, max = 9, message  = "Debe ser de largo 9")
	@Schema(description = "Número de teléfono del usuario", example = "123456789")
    private String number;
	
	@Pattern(regexp = "\\d+", message = "Debe contener solo números")
	@NotEmpty
	@Size(min = 2, max = 2, message  = "Debe ser de largo 2")
	 @Schema(description = "Código de ciudad del teléfono", example = "12")
    private String citycode;
	
	@Pattern(regexp = "\\d+", message = "Debe contener solo números")
	@NotEmpty
	@Size(min = 2, max = 2, message  = "Debe ser de largo 2")
	@Schema(description = "Código de país del teléfono", example = "34")
    private String contrycode;

}
