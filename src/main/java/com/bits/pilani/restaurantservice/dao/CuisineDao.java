package com.bits.pilani.restaurantservice.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.bits.pilani.restaurantservice.entity.CuisineEntity;

public interface CuisineDao extends ListCrudRepository<CuisineEntity, Integer> {
	
}
