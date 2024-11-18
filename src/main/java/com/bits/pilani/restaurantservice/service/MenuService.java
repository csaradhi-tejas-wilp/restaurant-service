package com.bits.pilani.restaurantservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.bits.pilani.restaurantservice.exception.CustomException;
import com.bits.pilani.restaurantservice.dao.CuisineDao;
import com.bits.pilani.restaurantservice.dao.MenuCategoryDao;
import com.bits.pilani.restaurantservice.dao.MenuDao;
import com.bits.pilani.restaurantservice.dao.RestaurantDao;
import com.bits.pilani.restaurantservice.entity.MenuItemEntity;
import com.bits.pilani.restaurantservice.entity.RestaurantEntity;
import com.bits.pilani.restaurantservice.to.MenuItemTO;
import com.bits.pilani.restaurantservice.to.RestaurantTO;
import com.bits.pilani.restaurantservice.to.SearchResultTO;

@Service
public class MenuService {
	
	@Autowired
	MenuDao menuDao;
	
	@Autowired
	RestaurantDao restaurantDao;
	
	@Autowired
	MenuCategoryDao menuCategoryDao;
	
	@Autowired
	CuisineDao cuisineDao;
	
    public MenuItemTO createMenu(MenuItemTO menuTO) throws CustomException {
    	try {
    		menuTO.setId(null);
    		MenuItemEntity menuEntity = new MenuItemEntity();    		
    		BeanUtils.copyProperties(menuTO, menuEntity);
    		menuEntity = menuDao.save(menuEntity);
    		return convertMenuEntityToMenuTO(menuEntity);
    	} catch(DataAccessException e) {
    		throw CustomException.INTERNAL_SERVER_ERRROR;
    	}
    }

    public MenuItemTO updateMenu(MenuItemTO menuTO) throws CustomException {
    	return createMenu(menuTO);
    }

    public void deleteMenu(@PathVariable int menuId) throws CustomException {
    	try {
    		menuDao.deleteById(menuId);    		
    	} catch(DataAccessException e) {
    		throw CustomException.INTERNAL_SERVER_ERRROR;
    	}
    }
    
    public List<MenuItemTO> getMenuForRestaurant(int restaurantId)  throws CustomException {
    	try {
    		List<MenuItemEntity> menuEntities = menuDao.findByRestaurantId(restaurantId);
    		return menuEntities.stream().map((menuEntity) -> {
    			MenuItemTO menuTO = convertMenuEntityToMenuTO(menuEntity);
    			return menuTO;
    		}).toList();    		
    	} catch(DataAccessException e) {
    		throw CustomException.INTERNAL_SERVER_ERRROR;
    	}
    }
    
    public List<MenuItemTO> getMenuForRestaurant(int restaurantId, Map<String, String> filter) throws CustomException {
    	
    	try {			
    		List<MenuItemTO> menu = getMenuForRestaurant(restaurantId);
    		
    		List<MenuItemTO> filteredMenu = menu;
    		
    		List<Predicate<MenuItemTO>> menuMatchers = new ArrayList<>();
    		
    		if(filter.containsKey("pure-veg")) {
    			boolean isPureVeg = Boolean.valueOf(filter.get("pure-veg"));	    			
    			menuMatchers.add((menuTO) -> {
    				return menuTO.isPureVeg() == isPureVeg;
    			});
    		}
    		
    		if(filter.containsKey("cuisine")) {
    			int cuisineId = Integer.valueOf(filter.get("cuisine"));
    			menuMatchers.add((menuTO) -> {
    				return menuTO.getCuisineId() == cuisineId;
    			});
    		}
    		
    		if(filter.containsKey("itemName")) {
    			String nameStr = filter.get("itemName");
    			menuMatchers.add((menuTO) -> {
    				return menuTO.getName().contains(nameStr);
    			});
    		}
    		
    		Predicate<MenuItemTO> menuMatcher = menuMatchers.stream().reduce(x -> true, Predicate::and);
    		
    		filteredMenu = filteredMenu.stream()
    				.filter(menuMatcher)
    				.toList();
    		
    		return filteredMenu;
    		
		} catch (CustomException e) {
			throw e;
		} catch(Exception e) {
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
    }
    
    public List<SearchResultTO> searchMenu(Map<String, String> filter) throws CustomException {
    	
    	List<SearchResultTO> searchResults = new ArrayList<>();
    	
    	if(filter.size() == 0) return searchResults;
    	
    	try {
    		List<RestaurantEntity> restaurantEntities = restaurantDao.findAll();
    		
    		for (RestaurantEntity restaurantEntity : restaurantEntities) {
    			List<MenuItemTO> filteredMenu = getMenuForRestaurant(restaurantEntity.getId(), filter);     		
    			if(filteredMenu.size() > 0) {
    				SearchResultTO searchResultTO = new SearchResultTO();
    				RestaurantTO restaurantTO = new RestaurantTO();
    				BeanUtils.copyProperties(restaurantEntity, restaurantTO);
    				searchResultTO.setRestaurant(restaurantTO);
    				searchResultTO.setMenu(filteredMenu);
    				searchResults.add(searchResultTO);
    			}				
			}
    		    		
    		return searchResults;    		
    		
    	} catch(CustomException e) {
    		throw e;
    	} catch(DataAccessException e) {
    		throw CustomException.INTERNAL_SERVER_ERRROR;
    	}
    }
    
    private MenuItemTO convertMenuEntityToMenuTO(MenuItemEntity menuEntity) {
    	MenuItemTO menuTO = new MenuItemTO();
		BeanUtils.copyProperties(menuEntity, menuTO);		
    	return menuTO;
    }
    
    public void checkIfMenuItemIdExist(int menuItemId) throws CustomException {
    	if(!menuDao.existsById(menuItemId)) {
    		String errorMsg = String.format("Menu item id = '%s' does not exist", menuItemId);
    		throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
    	}
    }
    
    public void validateMenuTO(MenuItemTO menuTO) throws CustomException {

    	if(Objects.isNull(menuTO.getName())) {
    		throw new CustomException(HttpStatus.BAD_REQUEST, "Menu item name is missing. Please provide menu item name.");
    	}

    	if(Objects.isNull(menuTO.getRestaurantId())) {
    		throw new CustomException(HttpStatus.BAD_REQUEST, "Restaurant id is missing. Please provide restaurant id.");
    	}

    	if(Objects.isNull(menuTO.getCuisineId())) {
    		throw new CustomException(HttpStatus.BAD_REQUEST, "Cuisine id is missing. Please provide cuisine id.");
    	}
    	
    	if(Objects.isNull(menuTO.getCategoryId())) {
    		throw new CustomException(HttpStatus.BAD_REQUEST, "Menu category id is missing. Please provide menu category id.");
    	}
    	
    	if(!restaurantDao.existsById(menuTO.getRestaurantId())) {
    		String errorMsg = String.format("Restaurant id = '%s' is invalid", menuTO.getRestaurantId());
    		throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
    	}
    	
    	if(!cuisineDao.existsById(menuTO.getCuisineId())) {
    		String errorMsg = String.format("Cuisine category id = '%s' is invalid", menuTO.getCuisineId());
    		throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
    	}
    	
    	if(!menuCategoryDao.existsById(menuTO.getCategoryId())) {
    		String errorMsg = String.format("Menu category id = '%s' is invalid", menuTO.getCategoryId());
    		throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
    	}
    }
}
