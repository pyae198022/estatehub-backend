package com.estatehub.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.dto.Input.RegisterRequest;
import com.estatehub.backend.model.entity.User;
import com.estatehub.backend.model.entity.UserProfile;
import com.estatehub.backend.model.repo.UserProfileRepo;
import com.estatehub.backend.model.repo.UserRepo;
import com.estatehub.backend.utils.AppBussinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepo userRepo;
    private final UserProfileRepo profileRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {
    	
    	if (userRepo.findByEmail(request.email()).isPresent()) {
            throw new AppBussinessException("Email already exists!");
        }
        var user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        
        userRepo.save(user);

        var profile = new UserProfile();
        profile.setUser(user);
        profile.setFullName(request.fullName());
        
        profileRepo.save(profile);
    }
    
    @Transactional
    public String login(String email, String password) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new AppBussinessException("User not found"));
        
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return jwtService.generateToken(email);
    }
}
