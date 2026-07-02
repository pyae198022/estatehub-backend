package com.estatehub.backend.model.dto.Input;

public record LoginRequest(
	    String email, 
	    String password
	) {}