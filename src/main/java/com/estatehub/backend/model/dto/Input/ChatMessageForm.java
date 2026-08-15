package com.estatehub.backend.model.dto.Input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatMessageForm(
		@NotBlank(message = "Message cannot be empty") @Size(max = 2000, message = "Message is too long") String content) {
}