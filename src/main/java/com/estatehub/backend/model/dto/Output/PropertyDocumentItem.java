package com.estatehub.backend.model.dto.Output;

import java.time.LocalDateTime;

import com.estatehub.backend.model.entity.PropertyDocument;

public record PropertyDocumentItem(
		Long id,
		String documentName,
		String documentUrl,
		LocalDateTime uploadedAt
		) {

	public static PropertyDocumentItem from(PropertyDocument doc) {
        return new PropertyDocumentItem(
            doc.getId(),
            doc.getDocumentName(),
            doc.getDocumentUrl(),
            doc.getUploadedAt()
        );
    }
}