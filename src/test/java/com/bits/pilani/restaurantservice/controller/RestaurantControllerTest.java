package com.bits.pilani.restaurantservice.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.service.MenuService;
import com.bits.pilani.restaurantservice.service.RestaurantService;
import com.bits.pilani.restaurantservice.to.CuisineTO;
import com.bits.pilani.restaurantservice.to.MenuCategoryTO;
import com.bits.pilani.restaurantservice.to.MenuItemTO;
import com.bits.pilani.restaurantservice.to.RestaurantTO;
import com.bits.pilani.restaurantservice.security.JwtAuthHandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { RestaurantController.class })
public class RestaurantControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	RestaurantService restaurantService;

	@MockBean
	MenuService menuService;

	@MockBean
	JwtAuthHandlerInterceptor jwtInterceptor;

	@Autowired
	ObjectMapper mapper;

	@BeforeEach
	void before() throws Exception {
		when(jwtInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
	}

	@Order(1)
	@Test
	void testGetAllRestaurant() throws Exception {
		var restaurant = new RestaurantTO();
		when(restaurantService.getAllRestaurants()).thenReturn(List.of(restaurant));
		mockMvc.perform(MockMvcRequestBuilders.get("/restaurant")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Order(2)
	@Test
	void testGetRestaurantByOwnerId() throws Exception {
		var restaurant = new RestaurantTO();
		when(restaurantService.getRestaurantByOwnerId(Mockito.anyInt())).thenReturn(List.of(restaurant));
		mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/1")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Order(3)
	@Test
	void testCreateRestaurant_Success() throws Exception {
		var restaurant = new RestaurantTO();
		when(restaurantService.createRestaurant(Mockito.any())).thenReturn(restaurant);
		var requestBody = mapper.writeValueAsString(restaurant);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/restaurant").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Order(3)
	@Test
	void testCreateRestaurant_Failure() throws Exception {
		var restaurant = new RestaurantTO();
		when(restaurantService.createRestaurant(Mockito.any())).thenReturn(restaurant);
		doThrow(new CustomException(HttpStatus.BAD_REQUEST, "Restaurant name is missing. Please provide restaurant name.")).when(restaurantService).validateRestaurantTO(Mockito.any());

		var requestBody = mapper.writeValueAsString(restaurant);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/restaurant").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Order(4)
	@Test
	void testUpdateRestaurant() throws Exception {
		var restaurant = new RestaurantTO();
		when(restaurantService.updateRestaurant(Mockito.any())).thenReturn(restaurant);		
		var requestBody = mapper.writeValueAsString(restaurant);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/restaurant/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(5)
	@Test
	void testDeleteRestaurant() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/restaurant/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(6)
	@Test
	void testSearchRestaurant() throws Exception {
		var restaurant = new RestaurantTO();
		when(restaurantService.searchRestaurant(Mockito.anyMap())).thenReturn(List.of(restaurant));		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/restaurant/search").queryParam("cuisine", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(7)
	@Test
	void testGetRestaurantMenu() throws Exception {
		var menuItemTO = new MenuItemTO();
		when(menuService.getMenuForRestaurant(Mockito.anyInt())).thenReturn(List.of(menuItemTO));		
		mockMvc.perform(
				MockMvcRequestBuilders.get("/restaurant/1/menu"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(8)
	@Test
	void testGetCuisines() throws Exception {
		var cuisine = new CuisineTO();
		when(restaurantService.getCuisines()).thenReturn(List.of(cuisine));
		mockMvc.perform(
				MockMvcRequestBuilders.get("/restaurant/cuisines"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	@Order(9)
	@Test
	void testGetMenuCategories() throws Exception {
		var menuCategory = new MenuCategoryTO();
		when(restaurantService.getMenuCategories()).thenReturn(List.of(menuCategory));
		mockMvc.perform(
				MockMvcRequestBuilders.get("/restaurant/menuCategories"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}
}
