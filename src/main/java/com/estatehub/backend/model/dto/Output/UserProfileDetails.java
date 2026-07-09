package com.estatehub.backend.model.dto.Output;

import com.estatehub.backend.model.entity.UserProfile;

public record UserProfileDetails(
		String fullName,
	    String profileImageUrl,
	    String bio,
	    String email, 
	    String phone
		) {
	
	public static UserProfileDetails from(UserProfile profile) {
        return new UserProfileDetails(
            profile.getFullName(),
            profile.getProfileImageUrl(),
            profile.getBio(),
            profile.getUser().getEmail(),
            profile.getPhone()
        );
    }
}
