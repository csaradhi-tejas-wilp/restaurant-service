package com.bits.pilani.restaurantservice.controller;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bits.pilani.restaurantservice.service.MenuService;
import com.bits.pilani.restaurantservice.to.MenuItemTO;
import com.bits.pilani.restaurantservice.to.SearchResultTO;
import com.bits.pilani.restaurantservice.security.JwtAuthHandlerInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {MenuController.class})
public class MenuControllerTest {

	@MockBean
	MenuService menuService;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;

	@MockBean
	JwtAuthHandlerInterceptor jwtInterceptor;

	@BeforeEach
	void before() throws Exception {
		when(jwtInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
	}

	@Order(1)
	@Test
	void testCreateMenu() throws Exception {
		var menuItemTO = new MenuItemTO();
		when(menuService.createMenu(Mockito.any())).thenReturn(menuItemTO);
		var requestBody = mapper.writeValueAsString(menuItemTO);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/menu").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isCreated());		
	}
	
	@Order(2)
	@Test
	void testUpdateMenu() throws Exception {
		var menuItemTO = new MenuItemTO();
		when(menuService.updateMenu(Mockito.any())).thenReturn(menuItemTO);
		var requestBody = mapper.writeValueAsString(menuItemTO);
		mockMvc.perform(
				MockMvcRequestBuilders.put("/menu/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(3)
	@Test
	void testDeleteMenu() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/menu/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(4)
	@Test
	void testSearchMenu() throws Exception {
		var searchResult = new SearchResultTO();
		when(menuService.searchMenu(Mockito.anyMap())).thenReturn(List.of(searchResult));
		mockMvc.perform(
				MockMvcRequestBuilders.get("/menu/search"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}	
}
