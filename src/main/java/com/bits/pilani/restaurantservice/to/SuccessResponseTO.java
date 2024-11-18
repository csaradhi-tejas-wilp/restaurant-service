package com.bits.pilani.restaurantservice.to;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponseTO<T> implements ResponseTO {
	
	private T data;
	
	public SuccessResponseTO(T data) {
		this.data = data;
	}
	
	public static <T> ResponseEntity<ResponseTO> create(T data) {
		SuccessResponseTO<T> successResponseTO = new SuccessResponseTO<T>(data);
		return ResponseEntity.ok(successResponseTO);
	}
	
	public static <T> ResponseEntity<ResponseTO> create(T data, HttpStatus httpStatus) {
		SuccessResponseTO<T> successResponseTO = new SuccessResponseTO<T>(data);
		return ResponseEntity.status(httpStatus).body(successResponseTO);
	}
}
