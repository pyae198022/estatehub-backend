package com.estatehub.backend.model.dto.Input;

public record UserProfileForm(
		
		String fullName,
	    String profileImageUrl,
	    String bio,
	    String nrc,
	    String phone
	  
		) {

}
