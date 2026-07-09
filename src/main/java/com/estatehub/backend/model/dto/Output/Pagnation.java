package com.estatehub.backend.model.dto.Output;

import java.util.List;

public record Pagnation<T>(
	    List<T> data,
	    int page,
	    int size,
	    long totalElements,
	    int totalPages
	) {}
