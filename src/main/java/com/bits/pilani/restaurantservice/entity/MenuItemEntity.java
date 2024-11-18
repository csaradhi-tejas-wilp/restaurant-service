package com.bits.pilani.restaurantservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "MenuItem", schema = "public")
@Entity
@Getter @Setter
public class MenuItemEntity {
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column
	Integer restaurantId;
	
	@Column
	Integer categoryId;
	
	@Column
	Integer cuisineId;
	
	@Column
	String name;
	
	@Column
	String description;
	
	@Column
	int price;
	
	@Column
	boolean available;
	
	@Column
	Float rating = 0.0f;
	
	@Column
	int preparationTime;
	
	@Column
	boolean pureVeg;
}
