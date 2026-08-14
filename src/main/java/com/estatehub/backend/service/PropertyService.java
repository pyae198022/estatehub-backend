package com.estatehub.backend.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.dto.Input.PropertyForm;
import com.estatehub.backend.model.dto.Input.PropertySearch;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.dto.Output.Pagnation;
import com.estatehub.backend.model.dto.Output.PendingPropertyItem;
import com.estatehub.backend.model.dto.Output.PropertyDetails;
import com.estatehub.backend.model.dto.Output.PropertyListItem;
import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.PropertyImage;
import com.estatehub.backend.model.repo.PropertyRepo;
import com.estatehub.backend.model.repo.UserProfileRepo;
import com.estatehub.backend.model.repo.UserRepo;
import com.estatehub.backend.utils.AppBussinessException;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PropertyService {
	private final EntityManager entityManager;
	private final UserRepo userRepository;
	private final PropertyRepo propertyRepository;
	private final UserProfileRepo profileRepo;
	
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
	public Pagnation<PropertyListItem> search(PropertySearch search) {

	    long totalElements = countSearch(search);
	    
	    var list = propertyRepository.search(cb -> {
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
	    }, search); 

	    // Handle null values with defaults
	    int pageSize = search.size() != null ? search.size() : 9;
	    int pageNumber = search.page() != null ? search.page() : 0;
	    int totalPages = (int) Math.ceil((double) totalElements / pageSize);

	    return new Pagnation<>(list, pageNumber, pageSize, totalElements, totalPages);
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
	public ModificationResult<Long> rejectById(Long id) {
	    var entity = propertyRepository.findById(id)
	            .orElseThrow(() -> new AppBussinessException("There is no property with id %d".formatted(id)));
	    
	    entity.setStatus("REJECTED");
	    return new ModificationResult<>(true, id, "Property with id %d has been rejected.".formatted(id));
	}

	public List<PendingPropertyItem> pendingList() {
	    return propertyRepository.findByStatusOrderByCreatedAtDesc("PENDING").stream()
	            .map(property -> PendingPropertyItem.from(property,
	                    property.getOwner() != null
	                            ? profileRepo.findByUserId(property.getOwner().getId()).orElse(null)
	                            : null))
	            .toList();
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
	
	public long countSearch(PropertySearch search) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(Long.class);
        var root = cq.from(Property.class);
        
        cq.select(cb.count(root));
        cq.where(search.where(cb, root));
        
        return entityManager.createQuery(cq).getSingleResult();
    }
}
