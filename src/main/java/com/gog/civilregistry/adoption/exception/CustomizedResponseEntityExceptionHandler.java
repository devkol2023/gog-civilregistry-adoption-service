package com.gog.civilregistry.adoption.exception;
//package com.pwc.egovernance.exception;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//
//
//
//@ControllerAdvice
//public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
//
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(
//	  MethodArgumentNotValidException ex, 
//	  HttpHeaders headers, 
//	  HttpStatus status, 
//	  WebRequest request) {
//	    List<String> errors = new ArrayList<String>();
//	    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//	        errors.add(error.getField() + ": " + error.getDefaultMessage());
//	    }
//	    for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
//	        errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
//	    }
//	    
//	    ErrorDetails errorDetails = 
//	      new ErrorDetails( HttpStatus.BAD_REQUEST.value(), ex.getLocalizedMessage(), errors);
//	    return handleExceptionInternal(
//	      ex, errorDetails, headers, HttpStatus.BAD_REQUEST, request);
//	}
//	
//	
//	
//}
