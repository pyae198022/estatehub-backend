package com.estatehub.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estatehub.backend.model.dto.Input.UserProfileForm;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.dto.Output.UserProfileDetails;
import com.estatehub.backend.service.AuthService;
import com.estatehub.backend.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileApi {

	private final UserProfileService profileService;

    @GetMapping
    public UserProfileDetails getMyProfile() {
    	var userId = AuthService.getCurrentUserId();
        return profileService.findByUserId(userId);
    }

    @PutMapping
    public ModificationResult<Long> updateMyProfile(
            @RequestBody UserProfileForm request) {
    	
    	var userId = AuthService.getCurrentUserId();
        return profileService.update(userId, request);
    }
}
