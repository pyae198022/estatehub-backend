package com.estatehub.backend.model.dto.Output;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.UserProfile;

public record PendingPropertyItem(
		Long id,
		String title,
		String propertyType,
		String listingType,
		BigDecimal price,
		String township,
		String city,
		String ownerEmail,
		String ownerName,
		LocalDateTime createdAt
	) {

	public static PendingPropertyItem from(Property entity, UserProfile ownerProfile) {
		return new PendingPropertyItem(
			entity.getId(),
			entity.getTitle(),
			entity.getPropertyType(),
			entity.getListingType(),
			entity.getPrice(),
			entity.getTownship(),
			entity.getCity(),
			entity.getOwner() != null ? entity.getOwner().getEmail() : null,
			ownerProfile != null ? ownerProfile.getFullName() : null,
			entity.getCreatedAt()
		);
	}
}