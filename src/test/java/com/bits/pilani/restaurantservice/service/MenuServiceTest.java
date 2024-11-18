package com.bits.pilani.restaurantservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.dao.MenuCategoryDao;
import com.bits.pilani.restaurantservice.dao.MenuDao;
import com.bits.pilani.restaurantservice.dao.RestaurantDao;
import com.bits.pilani.restaurantservice.entity.MenuItemEntity;
import com.bits.pilani.restaurantservice.entity.RestaurantEntity;
import com.bits.pilani.restaurantservice.to.MenuItemTO;

@SpringBootTest
public class MenuServiceTest {
	
	@MockBean
	RestaurantDao restaurantDao;
	
	@MockBean
	MenuCategoryDao menuCategoryDao;
	
	@MockBean
	MenuDao menuDao;
	
	@SpyBean
	MenuService menuService;
		
	@Order(1)
	@Test
	void testCreateMenu() throws Exception {
		var menuItemEntity = new MenuItemEntity();
		menuItemEntity.setId(1);
		
		when(menuDao.save(Mockito.any())).thenReturn(menuItemEntity);
		
		var menuItemTO = new MenuItemTO();
		menuItemTO = menuService.createMenu(menuItemTO);
		
		assertNotNull(menuItemTO);
		assertEquals(menuItemEntity.getId(), menuItemTO.getId());
	}
	
	@Order(2)
	@Test
	void testUpdateMenu() throws Exception {
		var menuItemEntity = new MenuItemEntity();
		menuItemEntity.setId(1);
		
		when(menuDao.save(Mockito.any())).thenReturn(menuItemEntity);
		
		var menuItemTO = new MenuItemTO();
		menuItemTO = menuService.updateMenu(menuItemTO);
		
		assertNotNull(menuItemTO);
		assertEquals(menuItemEntity.getId(), menuItemTO.getId());
	}
	
	@Order(3)
	@Test
	void testGetMenuForRestaurant() throws Exception {
		var menuItemEntity = new MenuItemEntity();	
		when(menuDao.findByRestaurantId(Mockito.anyInt())).thenReturn(List.of(menuItemEntity));
		var menuItemTOs = menuService.getMenuForRestaurant(1);
		assertEquals(1, menuItemTOs.size());
	}
	
	@Order(4)
	@Test
	void testGetMenuForRestaurant_WithFilter() throws Exception {
		var menuItemEntityA = new MenuItemEntity();	
		menuItemEntityA.setCuisineId(1);

		var menuItemEntityB = new MenuItemEntity();	
		menuItemEntityB.setCuisineId(2);

		when(menuDao.findByRestaurantId(Mockito.anyInt())).thenReturn(List.of(menuItemEntityA, menuItemEntityB));
		
		var filter = Map.of("cuisine", "1");
		
		var menuItemTOs = menuService.getMenuForRestaurant(1, filter);
		assertEquals(1, menuItemTOs.size());
		assertEquals(1, menuItemTOs.get(0).getCuisineId());
	}
	
	@Order(5)
	@Test
	void testSearchMenu() throws Exception {
		var restaurantEntityA = new RestaurantEntity();		
		restaurantEntityA.setId(1);
		restaurantEntityA.setName("tasty bites");
		restaurantEntityA.setRating(4.2f);
		
		var restaurantEntityB = new RestaurantEntity();
		restaurantEntityB.setId(2);
		restaurantEntityB.setName("grandma's kitchen");
		restaurantEntityB.setRating(3.0f);
		
		var menuItemTO = new MenuItemTO();
		menuItemTO.setCuisineId(1);
							
		doReturn(List.of(menuItemTO)).doReturn(List.of()).when(menuService).getMenuForRestaurant(Mockito.anyInt(), Mockito.any());
		
		when(restaurantDao.findAll()).thenReturn(List.of(restaurantEntityA, restaurantEntityB));
		
		var serachResult = menuService.searchMenu(Map.of("cuisine", "1"));
		
		assertEquals(1, serachResult.size());
		assertEquals("tasty bites", serachResult.get(0).getRestaurant().getName());
	}
	
	@Order(6)
	@Test
    public void testCheckIfMenuItemIdExist_Failure() throws Exception {
		
		when(menuDao.existsById(Mockito.anyInt())).thenReturn(false);
		
		CustomException exception = assertThrows(CustomException.class, () -> {
			menuService.checkIfMenuItemIdExist(-1);
		});
				
		assertEquals("Menu item id = '-1' does not exist", exception.getMessage());
    }
	
	@Order(7)
	@Test
    public void testValidateMenuTO_MissingProperty() throws Exception {
		var menuItemTo = new MenuItemTO();

		CustomException exception = assertThrows(CustomException.class, () -> {
			menuService.validateMenuTO(menuItemTo);
		});
				
		assertEquals("Menu item name is missing. Please provide menu item name.", exception.getMessage());		
	}
}
