package com.estatehub.backend.model.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.Property;

public record PropertyDetails(
		Long id,
	    String title,
	    String description,
	    String propertyType,
	    String listingType,
	    BigDecimal price,
	    String township,
	    String city,
	    String status,
	    Long ownerId

		) {

	public static PropertyDetails from(Property entity) {
        if (entity == null) {
            return null;
        }
        
        return new PropertyDetails(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getPropertyType(),
            entity.getListingType(),
            entity.getPrice(),
            entity.getTownship(),
            entity.getCity(),
            entity.getStatus(),
            entity.getOwner() != null ? entity.getOwner().getId() : null
        );
    }
}
