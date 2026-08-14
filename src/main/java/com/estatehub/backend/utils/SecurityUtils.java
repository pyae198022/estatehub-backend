package com.estatehub.backend.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.estatehub.backend.model.entity.User;
import com.estatehub.backend.model.enums.UserRoles;

@Component
public class SecurityUtils {

    public static Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user.getId();
        }
        throw new AppBussinessException("User is not authenticated");
    }

    public static boolean isAdmin() {
        return currentUser() != null && currentUser().getRole() == UserRoles.ADMIN;
    }

    public static User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        return null;
    }
}
