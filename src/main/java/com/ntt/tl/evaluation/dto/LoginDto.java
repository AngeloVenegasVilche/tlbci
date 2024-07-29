package com.ntt.tl.evaluation.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotEmpty
    @Size(min = 1, max = 50)
    private String username;

    @NotEmpty
    @Size(min = 4, max = 100)
    private String password;

}