package com.estatehub.backend.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estatehub.backend.model.dto.Input.PropertyForm;
import com.estatehub.backend.model.dto.Input.PropertySearch;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.dto.Output.Pagnation;
import com.estatehub.backend.model.dto.Output.PendingPropertyItem;
import com.estatehub.backend.model.dto.Output.PropertyDetails;
import com.estatehub.backend.model.dto.Output.PropertyDocumentItem;
import com.estatehub.backend.model.dto.Output.PropertyListItem;
import com.estatehub.backend.service.PropertyService;
import com.estatehub.backend.utils.SecurityUtils;

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

    @GetMapping("/mine")
    public List<PropertyListItem> myListings() {
        return propertyService.myListings(SecurityUtils.getCurrentUserId());
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
    
    @GetMapping("/admin/pending")
    public List<PendingPropertyItem> pendingProperties() {
        return propertyService.pendingList();
    }

    @PutMapping("/admin/{id}/approve")
    public ModificationResult<Long> adminApprove(@PathVariable Long id) {
        return propertyService.approveById(id);
    }

    @PutMapping("/admin/{id}/reject")
    public ModificationResult<Long> adminReject(@PathVariable Long id) {
        return propertyService.rejectById(id);
    }
    
    @PostMapping("/{id}/images")
    public ModificationResult<Long> uploadImages(
            @PathVariable Long id, 
            @RequestBody List<String> imageUrls,
            @RequestParam(required = false, defaultValue = "0") Long coverIndex) {
        return propertyService.addImages(id, imageUrls, coverIndex);
    }

    @PostMapping(value = "/{id}/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModificationResult<Long> uploadImageFiles(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam(required = false) Long coverIndex) {
        return propertyService.uploadImages(id, files, coverIndex);
    }

    @GetMapping("/{id}/documents")
    public List<PropertyDocumentItem> propertyDocuments(@PathVariable Long id) {
        return propertyService.documents(id);
    }

    @PostMapping(value = "/{id}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ModificationResult<Long> uploadDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return propertyService.addDocument(id, file);
    }

    @DeleteMapping("/{id}/documents/{documentId}")
    public ModificationResult<Long> deleteDocument(
            @PathVariable Long id,
            @PathVariable Long documentId) {
        return propertyService.deleteDocument(id, documentId);
    }
}
