package com.bits.pilani.restaurantservice.dao;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;

import com.bits.pilani.restaurantservice.entity.MenuItemEntity;

public interface MenuDao extends ListCrudRepository<MenuItemEntity, Integer> {	
	List<MenuItemEntity> findByRestaurantId(int restaurantId);
}
