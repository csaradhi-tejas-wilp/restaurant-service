package com.bits.pilani.restaurantservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;

import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.dao.CuisineDao;
import com.bits.pilani.restaurantservice.dao.MenuCategoryDao;
import com.bits.pilani.restaurantservice.dao.RestaurantDao;
import com.bits.pilani.restaurantservice.entity.CuisineEntity;
import com.bits.pilani.restaurantservice.entity.MenuCategoryEntity;
import com.bits.pilani.restaurantservice.entity.RestaurantEntity;
import com.bits.pilani.restaurantservice.to.MenuItemTO;
import com.bits.pilani.restaurantservice.to.RestaurantTO;

@SpringBootTest
public class RestaurantServiceTest {

	@MockBean
	RestaurantDao restaurantDao;
	
	@MockBean
	CuisineDao cuisineDao;
	
	@MockBean
	MenuCategoryDao menuCategoryDao;
	
	@MockBean
	MenuService menuService;
	
	@Autowired
	RestaurantService restaurantService;

	@Order(1)
	@Test
	void testGetRestaurantByOwnerId() throws CustomException {
		var restaurantEntities = List.of(new RestaurantEntity());
		when(restaurantDao.findByOwnerId(Mockito.anyInt())).thenReturn(restaurantEntities);
		var restaurantTOs = restaurantService.getRestaurantByOwnerId(1);
		assertNotNull(restaurantTOs);
		assertEquals(1, restaurantTOs.size());
	}

	@Order(2)
	@Test
	void testGetAllRestaurants() throws Exception {
		var restaurantEntities = List.of(new RestaurantEntity());
		when(restaurantDao.findAll()).thenReturn(restaurantEntities);
		var restaurantTOs = restaurantService.getAllRestaurants();
		assertNotNull(restaurantTOs);
		assertEquals(1, restaurantTOs.size());
	}

	@Order(3)
	@Test
	void testCreateRestaurant_Success() throws Exception {
		RestaurantEntity restaurantEntity = new RestaurantEntity();
		restaurantEntity.setId(1);
		restaurantEntity.setName("restaurant");

		when(restaurantDao.save(Mockito.any(RestaurantEntity.class))).thenReturn(restaurantEntity);

		RestaurantTO resturantTO = restaurantService.createRestaurant(new RestaurantTO());

		assertNotNull(resturantTO);
		assertEquals(restaurantEntity.getId(), resturantTO.getId());
		assertEquals(restaurantEntity.getName(), resturantTO.getName());
	}

	@Order(3)
	@Test
	void testCreateRestaurant_InvalidOwner() throws Exception {
		DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException("error",
				new ConstraintViolationException("error", null, "foriegn key"));
		
		when(restaurantDao.save(Mockito.any(RestaurantEntity.class))).thenThrow(dataIntegrityViolationException);

		RestaurantTO resturantTO = new RestaurantTO();
		resturantTO.setOwnerId(1);

		CustomException exception = assertThrows(CustomException.class, () -> {
			restaurantService.createRestaurant(resturantTO);
		});

		assertEquals("owner id = '1' is invalid", exception.getMessage());
	}

	@Order(4)
	@Test
	void testUpdateRestaurant_Success() throws Exception {
		RestaurantEntity restaurantEntity = new RestaurantEntity();
		restaurantEntity.setId(1);
		restaurantEntity.setName("restaurant");

		when(restaurantDao.save(Mockito.any(RestaurantEntity.class))).thenReturn(restaurantEntity);

		RestaurantTO resturantTO = restaurantService.updateRestaurant(new RestaurantTO());

		assertNotNull(resturantTO);
		assertEquals(restaurantEntity.getId(), resturantTO.getId());
		assertEquals(restaurantEntity.getName(), resturantTO.getName());
	}

	@Order(5)
	@Test
	void testGetMenuFilterForRestaurantSearch() throws CustomException {
		var expected = Map.of("pure-veg", "true", "cuisine", "arabic");
		var input =  Map.of("pure-veg", "true", "cuisine", "arabic", "restaurantName", "tasty bites");
		var result = restaurantService.getMenuFilterForRestaurantSearch(input);
		assertEquals(expected, result);
	}
	
	@Order(6)
	@Test
	void testSearchRestaurant() throws CustomException {
		
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
					
		when(menuService.getMenuForRestaurant(Mockito.anyInt(), Mockito.any())).thenReturn(List.of(menuItemTO)).thenReturn(List.of());		
		when(restaurantDao.findAll()).thenReturn(List.of(restaurantEntityA, restaurantEntityB));
		
		var serachResult = restaurantService.searchRestaurant(Map.of("restaurantName", "tasty bites", "rating", "4.0", "cuisine", "1"));
		
		assertEquals(1, serachResult.size());
		assertEquals("tasty bites", serachResult.get(0).getName());
	}
	
	@Order(7)
	@Test
	void testGetCuisines() throws Exception {		
		when(cuisineDao.findAll()).thenReturn(List.of(new CuisineEntity()));		
		var cuisines = restaurantService.getCuisines();		
		assertEquals(1, cuisines.size());
	}

	@Order(8)
	@Test
	void testGetMenuCategories() throws Exception {		
		when(menuCategoryDao.findAll()).thenReturn(List.of(new MenuCategoryEntity()));		
		var menuCategories = restaurantService.getMenuCategories();		
		assertEquals(1, menuCategories.size());
	}
	
	@Order(9)
	@Test
	void testCheckIfRestaurantIdExist_Failure() throws Exception {

		when(restaurantDao.existsById(Mockito.anyInt())).thenReturn(false);
		
		CustomException exception = assertThrows(CustomException.class, () -> {
			restaurantService.checkIfRestaurantIdExist(-1);
		});

		assertEquals("restaurant id = '-1' is invalid", exception.getMessage());
	}
	
	@Order(10)
	@Test
	void testValidateRestaurantTO_MissingProperty() {		
		RestaurantTO restaurantTO = new RestaurantTO();
		
		CustomException exception = assertThrows(CustomException.class, () -> {
			restaurantService.validateRestaurantTO(restaurantTO);
		});

		assertEquals("Restaurant name is missing. Please provide restaurant name.", exception.getMessage());		
	}
}
