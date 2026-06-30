package com.estatehub.backend.model.dto.Response;
import java.math.BigDecimal;

import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.PropertyImage;
import com.estatehub.backend.model.entity.PropertyImage_;
import com.estatehub.backend.model.entity.Property_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public record PropertyListItem(
	    Long id,
	    String title,
	    BigDecimal price,
	    String township,
	    String city,
	    String propertyType,
	    String listingType,
	    String status,
	    String coverImageUrl
	) {
	    public static void select(CriteriaBuilder cb, CriteriaQuery<PropertyListItem> cq, Root<Property> root) {
	    	
	    	var imageSubquery = cq.subquery(String.class);
	    	var imageRoot = imageSubquery.from(PropertyImage.class);
	        
	    	imageSubquery.select(cb.least(imageRoot.get(PropertyImage_.imageUrl)))
            .where(
                cb.equal(imageRoot.get(PropertyImage_.property).get(Property_.id), root.get(Property_.id)),
                cb.equal(imageRoot.get(PropertyImage_.isCover), true)
            );

	        cq.select(cb.construct(
	            PropertyListItem.class,
	            root.get(Property_.id),
	            root.get(Property_.title),
	            root.get(Property_.price),
	            root.get(Property_.township),
	            root.get(Property_.city),
	            root.get(Property_.propertyType),
	            root.get(Property_.listingType),
	            root.get(Property_.status),
	            imageSubquery
	        ));
	    }
	}