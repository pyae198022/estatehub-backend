package com.estatehub.backend.model.dto.Input;

public record InquiryRequest(
		Long propertyId,
	    Long buyerId,
	    String message
		) {

}
