package com.estatehub.backend.service;
import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.dto.Input.PropertyForm;
import com.estatehub.backend.model.dto.Input.PropertySearch;
import com.estatehub.backend.model.dto.Response.ModificationResult;
import com.estatehub.backend.model.dto.Response.PropertyDetails;
import com.estatehub.backend.model.dto.Response.PropertyListItem;
import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.Property_;
import com.estatehub.backend.model.repo.PropertyRepo;
import com.estatehub.backend.model.repo.UserRepo;
import com.estatehub.backend.utils.AppBussinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PropertyService {
	
	private final  UserRepo userRepository;
	private final PropertyRepo propertyRepository;
	
	@Transactional
	public ModificationResult<Long> create(PropertyForm request) {

	    var owner = userRepository.findById(request.ownerId())
	            .orElseThrow(() -> new AppBussinessException("Owner not found"));

	    var property = request.entity();
	    property.setOwner(owner); 

	    var saved = propertyRepository.save(property);
	    return ModificationResult.success(saved.getId());
	}

	@Transactional
	public ModificationResult<Long> update(Long id, PropertyForm request) {
	    var property = propertyRepository.findById(id)
	            .orElseThrow(() -> new AppBussinessException("Property not found"));

	    request.update(property);
	    return ModificationResult.success(property.getId());
	}
	
	
	public List<PropertyListItem> search(PropertySearch search) {
        return propertyRepository.search(cb -> {
            var cq = cb.createQuery(PropertyListItem.class);
            var root = cq.from(Property.class);
            
            // DTO projection selection logic
            PropertyListItem.select(cb, cq, root);
            
            // Dynamic Where Clause
            cq.where(search.where(cb, root));
            
            // Dynamic Having Clause
            var havingPredicates = search.having(cb, root);
            if (havingPredicates.length > 0) {
                cq.having(havingPredicates);
            }
            
            // Order by Latest ID using Metamodel
            cq.orderBy(cb.desc(root.get(Property_.id)));
                    
            return cq;
        });
    }
	
	public PropertyDetails findById(Long id) {
        var entity = propertyRepository.findById(id)
                .orElseThrow(() -> new AppBussinessException("There is no property with id %d".formatted(id)));
        
        return PropertyDetails.from(entity);
    }
	
}
