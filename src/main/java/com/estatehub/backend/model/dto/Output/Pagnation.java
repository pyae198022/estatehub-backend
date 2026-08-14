package com.estatehub.backend.model.dto.Output;

import java.util.List;

public record Pagnation<T>(
	    List<T> content,
	    int page,
	    int size,
	    long totalElements,
	    int totalPages
	) {}
