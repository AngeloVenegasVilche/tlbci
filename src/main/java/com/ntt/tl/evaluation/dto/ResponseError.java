package com.ntt.tl.evaluation.dto;

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
public class ResponseError {
	private String message;

}
