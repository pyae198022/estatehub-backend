package com.estatehub.backend.model.dto.Output;

import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.InterestRequest;

public record InterestRequestItem(
		Long id,
		Long propertyId,
		String propertyTitle,
		Long requesterId,
		String requesterEmail,
		String requesterName,
		String message,
		String status,
		LocalDateTime createdAt
	) {

	public static InterestRequestItem from(InterestRequest request, String requesterName) {
		return new InterestRequestItem(
			request.getId(),
			request.getProperty() != null ? request.getProperty().getId() : null,
			request.getProperty() != null ? request.getProperty().getTitle() : null,
			request.getRequester() != null ? request.getRequester().getId() : null,
			request.getRequester() != null ? request.getRequester().getEmail() : null,
			requesterName,
			request.getMessage(),
			request.getStatus(),
			request.getCreatedAt()
		);
	}
}