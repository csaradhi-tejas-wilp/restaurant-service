package com.bits.pilani.restaurantservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "Cuisine", schema = "public")
@Entity
@Getter @Setter
public class CuisineEntity {

	@Id
	@Column
	int id;
	
	@Column
	String name;
}
