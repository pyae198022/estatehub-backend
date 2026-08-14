package com.estatehub.backend.model.dto.Output;

import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.UserProfile;

public record UserProfileDetails(
		Long id,
		String email, 
	    String fullName,
	    String profileImageUrl,
	    String bio,
	    String nrc,
	    String phone,
	    String role,
	    LocalDateTime createdAt
		) {
	
	public static UserProfileDetails from(UserProfile profile) {
        return new UserProfileDetails(
            profile.getUser().getId(),
            profile.getUser().getEmail(),
            profile.getFullName(),
            profile.getProfileImageUrl(),
            profile.getBio(),
            profile.getNrc(),
            profile.getPhone(),
            profile.getUser().getRole() != null ? profile.getUser().getRole().name() : null,
            profile.getUser().getCreatedAt()
        );
    }
}
