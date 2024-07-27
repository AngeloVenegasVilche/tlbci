package com.ntt.tl.evaluation.dto;

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
public class PhoneDto {
	private Integer id;
	
	@NotEmpty
	@Pattern(regexp = "\\d+", message = "Debe contener solo números")
	@Size(min = 9, max = 9, message  = "Debe ser de largo 9")
    private String number;
	
	@Pattern(regexp = "\\d+", message = "Debe contener solo números")
	@NotEmpty
	@Size(min = 2, max = 2, message  = "Debe ser de largo 2")
    private String citycode;
	
	@Pattern(regexp = "\\d+", message = "Debe contener solo números")
	@NotEmpty
	@Size(min = 2, max = 2, message  = "Debe ser de largo 2")
    private String contrycode;

}
