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
import com.estatehub.backend.model.entity.PropertyImage;
import com.estatehub.backend.model.entity.Property_;
import com.estatehub.backend.model.repo.PropertyRepo;
import com.estatehub.backend.model.repo.UserRepo;
import com.estatehub.backend.utils.AppBussinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PropertyService {
	private final UserRepo userRepository;
	private final PropertyRepo propertyRepository;
	
	@Transactional
	public ModificationResult<Long> create(PropertyForm request) {
	    var owner = userRepository.findById(request.ownerId())
	            .orElseThrow(() -> new AppBussinessException("Owner not found"));

	    var property = request.entity();
	    property.setOwner(owner); 
	    
	    property.setStatus("PENDING"); 

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
            PropertyListItem.select(cb, cq, root);
            
            cq.where(search.where(cb, root));
            
            var havingPredicates = search.having(cb, root);
            if (havingPredicates.length > 0) {
                cq.having(havingPredicates);
            }
            search.applySort(cb, cq, root);
            return cq;
        });
    }
	
	@Transactional
	public PropertyDetails findById(Long id) {
        var entity = propertyRepository.findById(id)
                .orElseThrow(() -> new AppBussinessException("There is no property with id %d".formatted(id)));
        entity.setViewCount(entity.getViewCount() + 1);
        return PropertyDetails.from(entity);
    }
	
	@Transactional
	public ModificationResult<Long> deleteById(Long id) {
	    var entity = propertyRepository.findById(id)
	            .orElseThrow(() -> new AppBussinessException("There is no property with id %d".formatted(id)));
	    
	    entity.setStatus("SOLD"); 
	    
	    return new ModificationResult<>(true, id, "Property with id %d has been successfully marked as SOLD.".formatted(id));
	}

	@Transactional
	public ModificationResult<Long> approveById(Long id) {
	    var entity = propertyRepository.findById(id)
	            .orElseThrow(() -> new AppBussinessException("There is no property with id %d".formatted(id)));
	    
	    entity.setStatus("AVAILABLE"); 
	    return new ModificationResult<>(true, id, "Property with id %d has been successfully approved and is now AVAILABLE.".formatted(id));
	}
	
	@Transactional
	public ModificationResult<Long> addImages(Long propertyId, List<String> imageUrls, Long coverImageIndex) {
	    var property = propertyRepository.findById(propertyId)
	            .orElseThrow(() -> new AppBussinessException("Property not found"));

	    for (int i = 0; i < imageUrls.size(); i++) {
	        var img = new PropertyImage();
	        img.setImageUrl(imageUrls.get(i));
	        img.setProperty(property);
	        
	        if (coverImageIndex != null && coverImageIndex == i) {
	            img.setCover(true);
	        }
	        
	        property.getImages().add(img);
	    }
	    return new ModificationResult<>(true, propertyId, "Images uploaded successfully.");
	}
}
