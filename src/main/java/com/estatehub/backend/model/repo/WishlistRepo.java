package com.estatehub.backend.model.repo;

import java.util.List;
import java.util.Optional;

import com.estatehub.backend.model.BaseRepo;
import com.estatehub.backend.model.entity.WishlistItem;

public interface WishlistRepo extends BaseRepo<WishlistItem, Long> {

	Optional<WishlistItem> findByUserIdAndPropertyId(Long userId, Long propertyId);

	List<WishlistItem> findByUserId(Long userId);
}
