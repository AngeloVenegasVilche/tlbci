package com.ntt.tl.evaluation.dto;

import java.util.List;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestUser {

	@NotEmpty
	@Size(min = 1, max = 100, message  = "No debe tener más de 100 caracteres")
    private String name;
	
	@NotEmpty
	@Size(min = 1, max = 100, message  = "No debe tener más de 100 caracteres")
	@Pattern(regexp = "[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}", message = "Correo no valido")
    private String email;
	
	@NotEmpty
	@Size(min = 1, max = 100, message  = "No debe tener más de 100 caracteres")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{4,6}$", message = "La contraseña no cumple los requisitos, 1 Mayuscula | 4 carecteres o 6 caracteres| al menos 1 número")
    private String password;
	
	@NotEmpty
	private List<String> roles;
	
	@NotEmpty
	@Valid
    private List<PhoneDto> phones;
}
