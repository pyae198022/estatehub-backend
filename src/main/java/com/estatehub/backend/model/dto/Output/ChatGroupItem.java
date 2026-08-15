package com.estatehub.backend.model.dto.Output;

import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.ChatGroup;

public record ChatGroupItem(
		Long id,
		Long interestId,
		Long propertyId,
		String propertyTitle,
		Long buyerId,
		String buyerName,
		Long sellerId,
		String sellerName,
		String lastMessage,
		LocalDateTime lastMessageAt,
		String status,
		LocalDateTime createdAt) {

	public static ChatGroupItem from(ChatGroup group, Long buyerId, String buyerName, Long sellerId,
			String sellerName, String lastMessage, LocalDateTime lastMessageAt) {
		var property = group.getInterestRequest().getProperty();
		return new ChatGroupItem(
				group.getId(),
				group.getInterestRequest().getId(),
				property.getId(),
				property.getTitle(),
				buyerId,
				buyerName,
				sellerId,
				sellerName,
				lastMessage,
				lastMessageAt,
				group.getStatus(),
				group.getCreatedAt());
	}
}