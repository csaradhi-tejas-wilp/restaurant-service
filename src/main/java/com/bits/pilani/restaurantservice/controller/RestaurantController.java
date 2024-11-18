package com.bits.pilani.restaurantservice.controller;

import static com.bits.pilani.restaurantservice.exception.CustomException.handleException;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.service.MenuService;
import com.bits.pilani.restaurantservice.service.RestaurantService;
import com.bits.pilani.restaurantservice.to.RestaurantTO;
import com.bits.pilani.restaurantservice.security.Authorize;
import com.bits.pilani.restaurantservice.security.Role;
import com.bits.pilani.restaurantservice.to.ResponseTO;
import com.bits.pilani.restaurantservice.to.SuccessResponseTO;

@RestController
@ResponseBody
@RequestMapping("/restaurant")
public class RestaurantController {
	
	@Autowired
	RestaurantService restaurantService;
	
	@Autowired
	MenuService menuService;
	
	@Authorize
    @GetMapping
    public ResponseEntity<ResponseTO> getAllRestaurant() {    	
    	try {
    		var restaurants = restaurantService.getAllRestaurants();
    		return SuccessResponseTO.create(restaurants);
    	} catch(CustomException customException) {
    		return handleException(customException);
    	}    	
    }
	
	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @GetMapping("/{ownerId}")
    public ResponseEntity<ResponseTO> getRestaurantByOwnerId(@PathVariable int ownerId) { 
    	try {
    		var restaurants = restaurantService.getRestaurantByOwnerId(ownerId);
    		return SuccessResponseTO.create(restaurants);
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }

	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @PostMapping
    public ResponseEntity<ResponseTO> createRestaurant(@RequestBody RestaurantTO restaurantTO) {
		try {
			restaurantService.validateRestaurantTO(restaurantTO);
			restaurantTO.setId(null);
			var restaurant = restaurantService.createRestaurant(restaurantTO);
			return SuccessResponseTO.create(restaurant, HttpStatus.CREATED);
		} catch(CustomException e) {
			return handleException(e);
		}
    }

	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @PutMapping("/{restaurantId}")
    public ResponseEntity<ResponseTO> updateRestaurant(@RequestBody RestaurantTO restaurantTO, @PathVariable int restaurantId) {
    	try {    		
			restaurantService.validateRestaurantTO(restaurantTO);
    		restaurantService.checkIfRestaurantIdExist(restaurantId);
    		restaurantTO.setId(restaurantId);
    		var updatedRestaurant = restaurantService.updateRestaurant(restaurantTO);
    		return SuccessResponseTO.create(updatedRestaurant);
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }
	
	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<ResponseTO> deleteRestaurant(@PathVariable int restaurantId) {
    	try {
    		restaurantService.checkIfRestaurantIdExist(restaurantId);
    		restaurantService.deleteRestaurant(restaurantId);
    		return SuccessResponseTO.create(restaurantId);    		
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }
	
	@Authorize(roles = {Role.CUSTOMER})
    @GetMapping("/search")
    public ResponseEntity<ResponseTO> searchRestaurant(@RequestParam Map<String, String> filter) {    	
    	try {
    		var restaurants = restaurantService.searchRestaurant(filter);
    		return SuccessResponseTO.create(restaurants);    		
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }

	@Authorize(roles = {Role.CUSTOMER, Role.RESTAURANT_OWNER})
    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<ResponseTO> getRestaurantMenu(@PathVariable int restaurantId) {
    	try {
    		restaurantService.checkIfRestaurantIdExist(restaurantId);
    		var menuItems = menuService.getMenuForRestaurant(restaurantId);
    		return SuccessResponseTO.create(menuItems);
    	} catch(CustomException e) {
    		return handleException(e);
    	}    	
    }
    
    @Authorize
    @GetMapping("/cuisines")
    public ResponseEntity<ResponseTO> getCuisines() {
    	try {
    		var cuisines = restaurantService.getCuisines();
    		return SuccessResponseTO.create(cuisines);    		
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }

    @Authorize
    @GetMapping("/menuCategories")
    public ResponseEntity<ResponseTO> getMenuCategories() {
    	try {
    		var menuCategories = restaurantService.getMenuCategories();
    		return SuccessResponseTO.create(menuCategories);    		
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }
}
