package com.ntt.tl.evaluation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestActivateAccount {
    private String email;
    private Boolean activate;
}
