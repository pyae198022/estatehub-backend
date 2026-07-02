package com.estatehub.backend.model.repo;

import java.util.Optional;

import com.estatehub.backend.model.BaseRepo;
import com.estatehub.backend.model.entity.User;

public interface UserRepo extends BaseRepo<User, Long>{

	Optional<User> findByEmail(String email);
}
