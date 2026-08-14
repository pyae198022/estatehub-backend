package com.estatehub.backend.model.dto.Input;

import com.estatehub.backend.model.enums.UserRoles;

public record RegisterRequest(
	    String email,
	    String password,
	    String fullName,
	    UserRoles role,
	    String bio
	) {
    // Convenience method to get the role with a default of BUYER
    public UserRoles getEffectiveRole() {
        return role != null ? role : UserRoles.BUYER;
    }
}