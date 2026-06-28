package com.estatehub.backend.model.dto.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PropertyResponse(
		Long id,
	    String title,
	    String description,
	    String propertyType,
	    String listingType,
	    BigDecimal price,
	    String status,
	    String township,
	    String city,
	    UserResponse owner, 
	    LocalDateTime createdAt,
	    LocalDateTime updatedAt
		) {

}
