package com.ntt.tl.evaluation.dto;

import java.util.List;

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
public class RequestUpdateUser {
	@NotEmpty
    private String idUser;
	@NotEmpty
    private String name;
	@NotEmpty
    private String email;
	@NotEmpty
    private boolean isActive;
	@NotEmpty
    private List<PhoneUpdateDto> phons;
}
