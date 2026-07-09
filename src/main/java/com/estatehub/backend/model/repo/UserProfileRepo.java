package com.estatehub.backend.model.repo;

import java.util.Optional;

import com.estatehub.backend.model.BaseRepo;
import com.estatehub.backend.model.entity.UserProfile;

public interface UserProfileRepo extends BaseRepo<UserProfile, Long> {

	Optional<UserProfile> findByUserId(Long userId);
}
