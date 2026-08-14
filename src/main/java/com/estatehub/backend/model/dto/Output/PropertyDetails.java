package com.estatehub.backend.model.dto.Output;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.PropertyImage;

public record PropertyDetails(
		Long id,
	    String title,
	    String description,
	    String propertyType,
	    String listingType,
	    BigDecimal price,
	    String township,
	    String city,
	    Double latitude,
	    Double longitude,
	    String status,
	    Long ownerId,
	    String ownerName,
	    String ownerEmail,
	    List<String> imageUrls,
	    int viewCount

		) {

	public static PropertyDetails from(Property entity, String ownerName, String ownerEmail) {
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
            entity.getLatitude(),
            entity.getLongitude(),
            entity.getStatus(),
            entity.getOwner() != null ? entity.getOwner().getId() : null,
            ownerName,
            ownerEmail,
            entity.getImages().stream().map(PropertyImage::getImageUrl).toList(),
            entity.getViewCount()
        );
    }
}
