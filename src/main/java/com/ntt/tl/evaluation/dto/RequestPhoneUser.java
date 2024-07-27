package com.ntt.tl.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(description = "Objeto de solicitud para agregar un teléfono a un usuario")
public class RequestPhoneUser {

    @NotEmpty
    @Schema(description = "ID del usuario", example = "user123")
    private String idUser;

    @Valid
    @Schema(description = "Objeto del teléfono", implementation = PhoneDto.class)
    private PhoneDto phone;
}