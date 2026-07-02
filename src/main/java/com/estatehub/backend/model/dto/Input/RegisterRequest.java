package com.estatehub.backend.model.dto.Input;

import com.estatehub.backend.model.enums.UserRoles;

public record RegisterRequest(
	    String email,
	    String password,
	    String fullName,
	    UserRoles role
	) {}