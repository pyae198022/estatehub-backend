package com.estatehub.backend.utils;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.estatehub.backend.model.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
    private final UserRepo userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws AppBussinessException {
		return (UserDetails) userRepo.findByEmail(email)
                .orElseThrow(() -> new AppBussinessException("User not found"));
    }

}
