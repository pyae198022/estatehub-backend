package com.estatehub.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estatehub.backend.model.dto.Response.ModificationResult;
import com.estatehub.backend.model.dto.Response.PropertyListItem;
import com.estatehub.backend.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistApi {

    private final WishlistService wishlistService;
    @PostMapping("/toggle")
    public ModificationResult<Long> toggle(@RequestParam Long userId, @RequestParam Long propertyId) {
        return wishlistService.toggleWishlist(userId, propertyId);
    }

    @GetMapping("/{userId}")
    public List<PropertyListItem> getWishlist(@PathVariable Long userId) {
        return wishlistService.getUserWishlist(userId);
    }
}