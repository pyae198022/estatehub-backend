package com.estatehub.backend.model.dto.Output;

import java.time.LocalDateTime;

import com.estatehub.backend.model.enums.UserRoles;

public record UserResponse(
		Long id,
	    String email,
	    String phone,
	    UserRoles role,
	    LocalDateTime createdAt
		) {

}
