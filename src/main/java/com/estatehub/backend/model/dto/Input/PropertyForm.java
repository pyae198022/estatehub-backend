package com.estatehub.backend.model.dto.Input;

import java.math.BigDecimal;

import com.estatehub.backend.model.entity.Property;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PropertyForm(
		@NotBlank(message = "Title is required")
	    String title,
	    
	    @NotBlank(message = "Description is required")
	    String description,
	    
	    @NotBlank(message = "Property type is required")
	    String propertyType,
	    
	    @NotBlank(message = "Listing type is required")
	    String listingType,
	    
	    @NotNull(message = "Price is required")
	    BigDecimal price,
	    
	    @NotBlank(message = "Township is required")
	    String township,
	    
	    @NotBlank(message = "City is required")
	    String city,
	    
	    @NotNull(message = "Owner ID is required")
	    Long ownerId
	) {

	    public Property entity() {
	        var entity = new Property();
	        update(entity);
	        entity.setStatus("AVAILABLE");
	        return entity;
	    }
	    public void update(Property entity) {
	        entity.setTitle(title);
	        entity.setDescription(description);
	        entity.setPropertyType(propertyType);
	        entity.setListingType(listingType);
	        entity.setPrice(price);
	        entity.setTownship(township);
	        entity.setCity(city);
	    }
}
