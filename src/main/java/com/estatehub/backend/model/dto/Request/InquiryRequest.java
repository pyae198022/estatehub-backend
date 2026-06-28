package com.estatehub.backend.model.dto.Request;

public record InquiryRequest(
		Long propertyId,
	    Long buyerId,
	    String message
		) {

}
