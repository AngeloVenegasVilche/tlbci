package com.ntt.tl.evaluation.errors;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class GenericException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 7424277983984826834L;
    private final HttpStatus httpStatus;

    public GenericException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
