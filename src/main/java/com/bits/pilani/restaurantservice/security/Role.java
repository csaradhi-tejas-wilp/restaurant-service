package com.bits.pilani.restaurantservice.security;

import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {

	CUSTOMER,
	RESTAURANT_OWNER, 
	DELIVERY_PERSONNEL,
	ADMIN;
		
	@JsonValue
	public Map<String, Object> getJsonValue() {
		int id = this.ordinal() + 1;
		return Map.of("id", id, "name", this.name());
	}

	public static Optional<Role> findById(int id) {
		for (Role role : Role.values()) {
			if((role.ordinal() + 1) == id) {
				return Optional.of(role);
			}
		}
		return Optional.empty();
	}
}
