package com.estatehub.backend.model.repo;

import java.util.List;

import com.estatehub.backend.model.BaseRepo;
import com.estatehub.backend.model.entity.PropertyDocument;

public interface PropertyDocumentRepo extends BaseRepo<PropertyDocument, Long> {

	List<PropertyDocument> findByPropertyIdOrderByUploadedAtDesc(Long propertyId);
}
