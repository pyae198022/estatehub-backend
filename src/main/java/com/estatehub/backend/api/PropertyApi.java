package com.estatehub.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estatehub.backend.model.dto.Input.PropertyForm;
import com.estatehub.backend.model.dto.Input.PropertySearch;
import com.estatehub.backend.model.dto.Response.ModificationResult;
import com.estatehub.backend.model.dto.Response.Pagnation;
import com.estatehub.backend.model.dto.Response.PropertyDetails;
import com.estatehub.backend.model.dto.Response.PropertyListItem;
import com.estatehub.backend.service.PropertyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyApi {

	private final PropertyService propertyService;
	
	@GetMapping("/search")
    public Pagnation<PropertyListItem> searchProperties(PropertySearch search) {
        return propertyService.search(search);
    }
	
	@GetMapping("/{id}")
    public PropertyDetails findByPropertyId(@PathVariable Long id) {
        return propertyService.findById(id);
    }
	
	@PostMapping
    public ModificationResult<Long> createProperty(@Valid @RequestBody PropertyForm request) {
        return propertyService.create(request);
    }

    @PutMapping("/{id}")
    public ModificationResult<Long> updateProperty(
            @PathVariable Long id, 
            @Valid @RequestBody PropertyForm request) {
        return propertyService.update(id, request);
    }
    @DeleteMapping("/{id}")
    public ModificationResult<Long> deleteProperty(@PathVariable Long id) {
        return propertyService.deleteById(id);
    }
    
    @PutMapping("/{id}/approve")
    public ModificationResult<Long> approveProperty(@PathVariable Long id) {
        return propertyService.approveById(id);
    }
    
    @PostMapping("/{id}/images")
    public ModificationResult<Long> uploadImages(
            @PathVariable Long id, 
            @RequestBody List<String> imageUrls,
            @RequestParam(required = false, defaultValue = "0") Long coverIndex) {
        return propertyService.addImages(id, imageUrls, coverIndex);
    }
}
