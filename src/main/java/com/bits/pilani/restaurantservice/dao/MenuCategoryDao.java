package com.bits.pilani.restaurantservice.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.bits.pilani.restaurantservice.entity.MenuCategoryEntity;

public interface MenuCategoryDao extends ListCrudRepository<MenuCategoryEntity, Integer> {

}
