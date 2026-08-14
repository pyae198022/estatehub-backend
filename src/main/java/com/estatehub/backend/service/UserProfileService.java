package com.estatehub.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.estatehub.backend.model.dto.Input.UserProfileForm;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.dto.Output.UserProfileDetails;
import com.estatehub.backend.model.repo.UserProfileRepo;
import com.estatehub.backend.utils.AppBussinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserProfileRepo profileRepository;
    private final FileStorageService fileStorageService;

    public UserProfileDetails findByUserId(Long userId) {
        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppBussinessException("Profile not found for user id %d".formatted(userId)));
        
        return UserProfileDetails.from(profile);
    }

    @Transactional
    public ModificationResult<Long> update(Long userId, UserProfileForm form) {
        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppBussinessException("Profile not found for user id %d".formatted(userId)));
        
        profile.setFullName(form.fullName());
        profile.setBio(form.bio());
        if (form.profileImageUrl() != null && !form.profileImageUrl().isBlank()) {
            profile.setProfileImageUrl(form.profileImageUrl());
        }
        profile.setNrc(form.nrc());
        profile.setPhone(form.phone());
        
        profileRepository.save(profile);
        
        return new ModificationResult<>(true, userId, "Profile updated successfully.");
    }

    @Transactional
    public ModificationResult<Long> updateProfileImage(Long userId, MultipartFile file) {
        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppBussinessException("Profile not found for user id %d".formatted(userId)));

        String newUrl = fileStorageService.storeProfileImage(file);
        fileStorageService.deleteIfExists(profile.getProfileImageUrl());
        profile.setProfileImageUrl(newUrl);

        profileRepository.save(profile);

        return new ModificationResult<>(true, userId, "Profile image updated successfully.");
    }
}
