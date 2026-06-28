package com.estatehub.backend.model.dto.Request;

import java.math.BigDecimal;

public record PropertyRequest(
		String title,
	    String description,
	    String propertyType, // APARTMENT, HOUSE, CONDO, LAND
	    String listingType,  // SALE, RENT
	    BigDecimal price,
	    String township,
	    String city,
	    Long ownerId
		) {

}
