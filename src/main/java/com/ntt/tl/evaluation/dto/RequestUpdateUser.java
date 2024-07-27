package com.ntt.tl.evaluation.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
@Schema(description = "Objeto de solicitud para actualizar un usuario")
public class RequestUpdateUser {

    @NotEmpty
    @Schema(description = "ID del usuario", example = "user123")
    private String idUser;

    @NotEmpty
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String name;

    @NotEmpty
    @Email(message = "Correo no válido")
    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;

    @Schema(description = "Estado activo del usuario", example = "true")
    private boolean isActive;

    @NotEmpty
    @Valid
    @ArraySchema(schema = @Schema(description = "Teléfonos del usuario para actualizar", implementation = PhoneUpdateDto.class))
    private List<PhoneUpdateDto> phons;
}