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
    @Schema(description = "pass", example = "Just2.")
    private String pass;

    @NotEmpty
    @Schema(description = "Nombre del usuario", example = "Juan Pérez")
    private String name;

    @Schema(description = "mail del usuario", example = "angelo.venegas@hotmail.com")
    private String email;
}