package com.ntt.tl.evaluation.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Schema(description = "Objeto de datos de usuario")
public class UserDto {

    @Schema(description = "Identificador del usuario", example = "user123")
    private String idUser;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    @Schema(description = "Fecha de creación del usuario", example = "01/01/2024 12:00:00")
    private Date created;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    @Schema(description = "Fecha de última modificación del usuario", example = "01/01/2024 12:00:00")
    private Date modified;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    @Schema(description = "Fecha de último inicio de sesión del usuario", example = "01/01/2024 12:00:00")
    private Date lastLogin;

    @Schema(description = "Token de autenticación del usuario", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
    private String token;

    @Schema(description = "Estado activo del usuario", example = "true")
    private Boolean isActive;

    @ArraySchema(schema = @Schema(description = "Lista de teléfonos del usuario", implementation = PhoneDto.class))
    private List<PhoneDto> phons;
}
