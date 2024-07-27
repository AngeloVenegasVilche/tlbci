package com.ntt.tl.evaluation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de solicitud para eliminar un teléfono de un usuario")
public class RequestDeletePhoneUser {

    @NotEmpty
    @Schema(description = "ID del usuario", example = "user123")
    private String idUser;

    @NotNull
    @Schema(description = "ID del teléfono", example = "1")
    private Integer idPhone;
}