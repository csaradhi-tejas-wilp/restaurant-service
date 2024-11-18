package com.bits.pilani.restaurantservice.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseTO implements ResponseTO {
	
	private int status;
	private String error;
	private String message;
	
	public ErrorResponseTO() {
		
	}
	
	public ErrorResponseTO(int status, String error, String message) {
		this.status = status;
		this.error = error;
		this.message = message;
	}
}
