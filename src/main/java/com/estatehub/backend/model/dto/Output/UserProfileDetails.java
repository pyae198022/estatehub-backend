package com.estatehub.backend.model.dto.Output;

import com.estatehub.backend.model.entity.UserProfile;

public record UserProfileDetails(
		Long id,
		String email, 
	    String fullName,
	    String profileImageUrl,
	    String bio,
	    String phone,
	    String role
		) {
	
	public static UserProfileDetails from(UserProfile profile) {
        return new UserProfileDetails(
            profile.getUser().getId(),
            profile.getUser().getEmail(),
            profile.getFullName(),
            profile.getProfileImageUrl(),
            profile.getBio(),
            profile.getPhone(),
            profile.getUser().getRole() != null ? profile.getUser().getRole().name() : null
        );
    }
}
