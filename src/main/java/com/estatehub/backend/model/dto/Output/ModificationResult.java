package com.estatehub.backend.model.dto.Output;


public record ModificationResult<ID>(
		boolean success,
		ID id,
		String message
		) {

	public static<ID> ModificationResult<ID> success(ID id) {
		return new ModificationResult<>(true, id, "Modification successful");
	}

}

