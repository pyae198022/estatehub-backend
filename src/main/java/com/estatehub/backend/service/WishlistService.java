package com.estatehub.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.dto.Response.ModificationResult;
import com.estatehub.backend.model.dto.Response.PropertyListItem;
import com.estatehub.backend.model.entity.WishlistItem;
import com.estatehub.backend.model.repo.PropertyRepo;
import com.estatehub.backend.model.repo.UserRepo;
import com.estatehub.backend.model.repo.WishlistRepo;
import com.estatehub.backend.utils.AppBussinessException;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepo wishlistRepository;
    private final UserRepo userRepository;
    private final PropertyRepo propertyRepository;

    @Transactional
    public ModificationResult
    <Long> toggleWishlist(Long userId, Long propertyId) {
        var existing = wishlistRepository.findByUserIdAndPropertyId(userId, propertyId);

        if (existing.isPresent()) {
            wishlistRepository.delete(existing.get());
            return new ModificationResult<>(true, propertyId, "Property removed from wishlist.");
        } else {

            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppBussinessException("User not found"));
            var property = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new AppBussinessException("Property not found"));

            var item = new WishlistItem();
            item.setUser(user);
            item.setProperty(property);
            wishlistRepository.save(item);

            return new ModificationResult<>(true, propertyId, "Property added to wishlist successfully.");
        }
    }


    public List<PropertyListItem> getUserWishlist(Long userId) {
        var items = wishlistRepository.findByUserId(userId);
        
        return items.stream()
                .map(item -> {
                    var p = item.getProperty();
                   
                    String cover = p.getImages().stream()
                            .filter(img -> img.isCover())
                            .map(img -> img.getImageUrl())
                            .findFirst().orElse(null);

                    return new PropertyListItem(
                        p.getId(), p.getTitle(), p.getPrice(), p.getTownship(), 
                        p.getCity(), p.getPropertyType(), p.getListingType(), p.getStatus(), cover
                    );
                }).toList();
    }
}
