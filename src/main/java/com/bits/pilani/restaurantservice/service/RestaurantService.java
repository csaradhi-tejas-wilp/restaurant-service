package com.bits.pilani.restaurantservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.dao.CuisineDao;
import com.bits.pilani.restaurantservice.dao.MenuCategoryDao;
import com.bits.pilani.restaurantservice.dao.RestaurantDao;
import com.bits.pilani.restaurantservice.entity.RestaurantEntity;
import com.bits.pilani.restaurantservice.to.CuisineTO;
import com.bits.pilani.restaurantservice.to.MenuCategoryTO;
import com.bits.pilani.restaurantservice.to.MenuItemTO;
import com.bits.pilani.restaurantservice.to.RestaurantTO;

@Service
public class RestaurantService {

	@Autowired
	RestaurantDao restaurantDao;

	@Autowired
	MenuService menuService;

	@Autowired
	CuisineDao cuisineDao;

	@Autowired
	MenuCategoryDao menuCategoryDao;

	public List<RestaurantTO> getRestaurantByOwnerId(int ownerId) throws CustomException {		
		try {
			var restaurantEntities = restaurantDao.findByOwnerId(ownerId);
			return restaurantEntities.stream().map((restaurantEntity) -> {
				RestaurantTO restaurantTO = new RestaurantTO();
				BeanUtils.copyProperties(restaurantEntity, restaurantTO);
				return restaurantTO;
			}).toList();
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public List<RestaurantTO> getAllRestaurants() throws CustomException {
		try {
			return restaurantDao.findAll().stream().map((restaurantEntity) -> {
				RestaurantTO restaurantTO = new RestaurantTO();
				BeanUtils.copyProperties(restaurantEntity, restaurantTO);
				return restaurantTO;
			}).toList();
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public RestaurantTO createRestaurant(RestaurantTO restaurantTO) throws CustomException {
		try {
			RestaurantEntity restaurantEntity = new RestaurantEntity();
			BeanUtils.copyProperties(restaurantTO, restaurantEntity);
			restaurantEntity = restaurantDao.save(restaurantEntity);
			BeanUtils.copyProperties(restaurantEntity, restaurantTO);
			return restaurantTO;
		} catch (DataAccessException e) {
			// checking for owner id constraint violation
			if(e.getCause() instanceof ConstraintViolationException ex) {
				System.out.println(ex.getMessage());
				String errorMsg = String.format("owner id = '%s' is invalid", restaurantTO.getOwnerId()); 
				throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
			} else {
				System.out.println(e.getMessage());
				throw CustomException.INTERNAL_SERVER_ERRROR;				
			}
		}
	}

	public RestaurantTO updateRestaurant(RestaurantTO restaurantTO) throws CustomException {				
		return createRestaurant(restaurantTO);
	}

	public void deleteRestaurant(@PathVariable int restaurantId) throws CustomException {
		try {
			restaurantDao.deleteById(restaurantId);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}
	
	private Set<String> menuFiltersAllowedInRestaurantSearch = Set.of("pure-veg", "cuisine");
			
	public Map<String, String> getMenuFilterForRestaurantSearch(Map<String, String> filter) {

		Map<String, String> menuFilter = new HashMap<>();
		
		filter.entrySet().forEach((keyValuePair) -> {
			if(menuFiltersAllowedInRestaurantSearch.contains(keyValuePair.getKey())) {
				menuFilter.put(keyValuePair.getKey(), keyValuePair.getValue());
			}
		});

		return menuFilter;
	}

	public List<RestaurantTO> searchRestaurant(Map<String, String> filter) throws CustomException {

		if (filter.size() == 0) {
			return new ArrayList<RestaurantTO>();
		}

		try {

			List<RestaurantEntity> restaurantEntities = restaurantDao.findAll();

			List<Predicate<RestaurantEntity>> restaurantMatchers = new ArrayList<>();

			if (filter.containsKey("rating")) {
				float rating = Float.valueOf(filter.get("rating"));
				restaurantMatchers.add((restaurantEntity) -> {
					return restaurantEntity.getRating() >= rating;
				});
			}

			if (filter.containsKey("restaurantName")) {
				String nameToFilterBy = filter.get("restaurantName");
				restaurantMatchers.add((restaurantEntity) -> {
					return restaurantEntity.getName().contains(nameToFilterBy);
				});
			}

			Predicate<RestaurantEntity> restaurantMatcher = restaurantMatchers.stream().reduce(x -> true,
					Predicate::and);
			
			Map<String, String> menuFilter = getMenuFilterForRestaurantSearch(filter);
			
			List<RestaurantEntity> filteredRestaurants = new ArrayList<>();
			
			for (RestaurantEntity restaurantEntity : restaurantEntities) {
				List<MenuItemTO> filteredMenu = menuService.getMenuForRestaurant(restaurantEntity.getId(), menuFilter);
				if(restaurantMatcher.test(restaurantEntity) && filteredMenu.size() > 0) {
					filteredRestaurants.add(restaurantEntity);
				}
			}
			
			var searchResult = filteredRestaurants.stream().map((restaurantEntity) -> {
				RestaurantTO restaurantTO = new RestaurantTO();
				BeanUtils.copyProperties(restaurantEntity, restaurantTO);
				return restaurantTO;
			}).toList();

			return searchResult;
			
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public List<CuisineTO> getCuisines() throws CustomException {
		try {			
			return cuisineDao.findAll().stream().map((cuisineEntity) -> {
				CuisineTO cuisineTO = new CuisineTO();
				BeanUtils.copyProperties(cuisineEntity, cuisineTO);
				return cuisineTO;
			}).toList();
		} catch(DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public List<MenuCategoryTO> getMenuCategories() throws CustomException {
		try {
			return menuCategoryDao.findAll().stream().map((menuCategoryEntity) -> {
				MenuCategoryTO menuCategoryTO = new MenuCategoryTO();
				BeanUtils.copyProperties(menuCategoryEntity, menuCategoryTO);
				return menuCategoryTO;
			}).toList();			
		} catch(DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}
	
	public void checkIfRestaurantIdExist(int restarauntId) throws CustomException {
		if(!restaurantDao.existsById(restarauntId)) {
			String errorMsg = String.format("restaurant id = '%s' is invalid", restarauntId);
			throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
		}
	}
	
	public void validateRestaurantTO(RestaurantTO restaurantTO) throws CustomException {
		
		if(	Objects.isNull(restaurantTO.getName())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Restaurant name is missing. Please provide restaurant name.");
		}
		
		if(Objects.isNull(restaurantTO.getAddress())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Restaurant address is missing. Please provide restaurant address.");			
		}
		
		if(Objects.isNull(restaurantTO.getOpeningHours())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Restaurant opening hour is missing. Please provide restaurant opening hour.");			
		}

		if(Objects.isNull(restaurantTO.getClosingHours())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Restaurant closing hour is missing. Please provide restaurant closing hour.");			
		}
		
		if(Objects.isNull(restaurantTO.getOwnerId())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Restaurant owner id is missing. Please provide restaurant owner id.");
		}		
	}
}
