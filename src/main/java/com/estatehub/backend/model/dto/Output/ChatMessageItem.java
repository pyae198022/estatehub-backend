package com.estatehub.backend.model.dto.Output;

import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.ChatMessage;

public record ChatMessageItem(
		Long id,
		Long groupId,
		Long senderId,
		String senderName,
		String content,
		LocalDateTime createdAt) {

	public static ChatMessageItem from(ChatMessage message, String senderName) {
		return new ChatMessageItem(
				message.getId(),
				message.getGroup().getId(),
				message.getSender().getId(),
				senderName,
				message.getContent(),
				message.getCreatedAt());
	}
}