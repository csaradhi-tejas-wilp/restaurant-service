package com.bits.pilani.restaurantservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bits.pilani.restaurantservice.to.ErrorResponseTO;
import com.bits.pilani.restaurantservice.to.ResponseTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomException extends Exception {

	private static final long serialVersionUID = -743914679689605383L;
	
	private HttpStatus httpStatus;
	private String message;
	
	public CustomException(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
	
	public static ResponseEntity<ResponseTO> handleException(CustomException customException) {
		ErrorResponseTO errorResponseTO = new ErrorResponseTO();
		errorResponseTO.setStatus(customException.getHttpStatus().value());
		errorResponseTO.setError(customException.getHttpStatus().name());
		errorResponseTO.setMessage(customException.getMessage());
		return ResponseEntity.status(customException.getHttpStatus()).body(errorResponseTO);
	}
	
	public static CustomException INTERNAL_SERVER_ERRROR = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Something happened while processing your request. Please try again later or contact administrator");
}
