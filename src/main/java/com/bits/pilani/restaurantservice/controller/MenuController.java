package com.bits.pilani.restaurantservice.controller;

import static com.bits.pilani.restaurantservice.exception.CustomException.handleException;

import java.util.List;
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
import com.bits.pilani.restaurantservice.to.MenuItemTO;
import com.bits.pilani.restaurantservice.to.SearchResultTO;
import com.bits.pilani.restaurantservice.security.Authorize;
import com.bits.pilani.restaurantservice.security.Role;
import com.bits.pilani.restaurantservice.to.ResponseTO;
import com.bits.pilani.restaurantservice.to.SuccessResponseTO;

@RestController
@ResponseBody
@RequestMapping("/restaurant/menu")
public class MenuController {

	@Autowired
	MenuService menuService;
	
	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @PostMapping
    public ResponseEntity<ResponseTO> createMenu(@RequestBody MenuItemTO menuTO) {
    	try {
    		menuService.validateMenuTO(menuTO);
    		menuTO.setId(null);
    		var createdMenuItem = menuService.createMenu(menuTO);
    		return SuccessResponseTO.create(createdMenuItem, HttpStatus.CREATED);
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }

	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @PutMapping("/{menuItemId}")
    public ResponseEntity<ResponseTO> updateMenu(@RequestBody MenuItemTO menuTO, @PathVariable int menuItemId) {
    	try {
    		menuService.checkIfMenuItemIdExist(menuItemId);
    		menuService.validateMenuTO(menuTO);
    		menuTO.setId(menuItemId);
    		var updatedMenuItem = menuService.updateMenu(menuTO);
    		return SuccessResponseTO.create(updatedMenuItem);
    	} catch(CustomException e) {
    		return handleException(e);
    	}
    }

	@Authorize(roles = {Role.RESTAURANT_OWNER})
    @DeleteMapping("/{menuItemId}")
    public ResponseEntity<ResponseTO> deleteMenu(@PathVariable int menuItemId) throws CustomException {
    	try {			
    		menuService.checkIfMenuItemIdExist(menuItemId);
    		menuService.deleteMenu(menuItemId);
    		return SuccessResponseTO.create(menuItemId);
		} catch (CustomException e) {
			return handleException(e);
		}
    }
	
	@Authorize(roles = {Role.CUSTOMER})
    @GetMapping("/search")
    public ResponseEntity<ResponseTO> searchMenu(@RequestParam Map<String, String> filter) {
    	try {
    		List<SearchResultTO> searchResults = menuService.searchMenu(filter); 
    		return SuccessResponseTO.create(searchResults);			
		} catch (CustomException e) {
			return handleException(e);
		}
    }
}
