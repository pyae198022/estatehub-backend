package com.estatehub.backend.model.repo;

import java.util.List;

import com.estatehub.backend.model.BaseRepo;
import com.estatehub.backend.model.entity.Property;

public interface PropertyRepo extends BaseRepo<Property, Long>{

	List<Property> findByStatusOrderByCreatedAtDesc(String status);

}
