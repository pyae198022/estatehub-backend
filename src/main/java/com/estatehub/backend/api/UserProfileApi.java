package com.estatehub.backend.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estatehub.backend.model.dto.Input.UserProfileForm;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.dto.Output.UserProfileDetails;
import com.estatehub.backend.service.UserProfileService;
import com.estatehub.backend.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/profile")
@RequiredArgsConstructor
public class UserProfileApi {

	private final UserProfileService profileService;

    @GetMapping
    public UserProfileDetails getMyProfile() {
    	var userId = SecurityUtils.getCurrentUserId();
        return profileService.findByUserId(userId);
    }

    @GetMapping("/{userId}")
    public UserProfileDetails getProfile(@PathVariable Long userId) {
        return profileService.findByUserId(userId);
    }

    @PutMapping
    public ModificationResult<Long> updateMyProfile(
            @RequestBody UserProfileForm request) {
    	
    	var userId = SecurityUtils.getCurrentUserId();
        return profileService.update(userId, request);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModificationResult<Long> uploadProfileImage(
            @RequestParam("file") MultipartFile file) {

        var userId = SecurityUtils.getCurrentUserId();
        return profileService.updateProfileImage(userId, file);
    }
}
