package com.estatehub.backend.model.dto.Response;


import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.Property_; // Generated Metamodel
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;

public record PropertyListItem(
	    Long id,
	    String title,
	    BigDecimal price,
	    String township,
	    String city,
	    String propertyType,
	    String listingType,
	    String status 
	) {
	    public static void select(CriteriaBuilder cb, CriteriaQuery<PropertyListItem> cq, Root<Property> root) {
	        cq.select(cb.construct(
	            PropertyListItem.class,
	            root.get(Property_.id),
	            root.get(Property_.title),
	            root.get(Property_.price),
	            root.get(Property_.township),
	            root.get(Property_.city),
	            root.get(Property_.propertyType),
	            root.get(Property_.listingType),
	            root.get(Property_.status)
	        ));
	    }
	}