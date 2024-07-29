package com.ntt.tl.evaluation.errors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ntt.tl.evaluation.util.ErrorUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author avenegas
 */
@Slf4j
@RestControllerAdvice
public class GenericExceptionHandler {
	
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ErrorResponse> handleGenericException(GenericException ex){
    	
        ErrorResponse errorResponse = ErrorResponse.builder()
            .mensaje(ex.getMessage())
            .build();
        
        log.error("GenericException : {}", ex.getMessage(), ex );
        
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
    	
    	
    	String result = ErrorUtil.getDetailError(ex.getBindingResult());
       
    	ErrorResponse errorResponse = ErrorResponse.builder()
                .mensaje(result)
                .build();
    	
    	log.error("Error - MethodArgumentNotValidException : {}", ex.getMessage(), ex );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .mensaje("Usuario no encontrado")
                .build();

        log.error("Error - UsernameNotFoundException: {}", ex.getMessage(), ex);

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    
    
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();
        
        log.error("Error - Exception : {}", ex.getMessage(), ex );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();

        log.error("Error - Exception : {}", ex.getMessage(), ex );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    

    @ExceptionHandler(Exception.class)
    @Order(Ordered.LOWEST_PRECEDENCE)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .mensaje(ex.getMessage())
                .build();
        
        log.error("Error - Exception : {}", ex.getMessage(), ex );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
