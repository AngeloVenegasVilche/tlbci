package com.ntt.tl.evaluation.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "Objeto de solicitud para crear un nuevo usuario")
public class RequestUser {

	@NotEmpty
	@Size(min = 1, max = 100, message  = "No debe tener más de 100 caracteres")
	@Schema(description = "Nombre del usuario", example = "Juan Pérez", maxLength = 100)
    private String name;
	
	@NotEmpty
	@Size(min = 1, max = 100, message  = "No debe tener más de 100 caracteres")
	 @Schema(description = "Correo electrónico del usuario", example = "user@example.com", maxLength = 100)
    private String email;
	
	@NotEmpty
	@Size(min = 1, max = 100, message  = "No debe tener más de 100 caracteres")
	@Schema(description = "Contraseña del usuario", example = "Password1", maxLength = 100)
    private String password;
	
	@NotEmpty
	@ArraySchema(schema = @Schema(description = "Roles del usuario", example = "ADMIN, USER, INVITED, EDITOR]"))
	private List<String> roles;
	
	@NotEmpty
	@Valid
	 @Schema(description = "Teléfonos del usuario")
    private List<PhoneDto> phones;
}
