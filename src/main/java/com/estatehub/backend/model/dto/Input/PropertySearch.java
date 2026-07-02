package com.estatehub.backend.model.dto.Input;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.Property_;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public record PropertySearch(
    String keyword,
    String township,
    String listingType,
    String propertyType,
    BigDecimal minPrice,
    BigDecimal maxPrice,
    String sortBy, 
    String order,
    Integer page,
    Integer size
) {
    public Predicate[] where(CriteriaBuilder cb, Root<Property> root) {
        var predicates = new ArrayList<Predicate>();
        // Keyword Search (Title သို့မဟုတ် Description တွင် တူညီမှု ရှိ၊ မရှိ)
        if (keyword != null && !keyword.isBlank()) {
            var likePattern = keyword.toLowerCase().concat("%");
            predicates.add(cb.or(
                cb.like(cb.lower(root.get(Property_.title)), likePattern),
                cb.like(cb.lower(root.get(Property_.description)), likePattern)
            ));
        }

        if (township != null && !township.isBlank()) {
            predicates.add(cb.equal(root.get(Property_.township), township));
        }

        if (listingType != null && !listingType.isBlank()) {
            predicates.add(cb.equal(root.get(Property_.listingType), listingType));
        }
        
        if (propertyType != null && !propertyType.isBlank()) {
            predicates.add(cb.equal(root.get(Property_.propertyType), propertyType)); 
        }

        if (minPrice != null) {
            predicates.add(cb.ge(root.get(Property_.price), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(cb.le(root.get(Property_.price), maxPrice));
        }

        // Active Status Criteria
        predicates.add(cb.equal(root.get(Property_.status), "AVAILABLE"));
       
        return predicates.toArray(size -> new Predicate[size]);
    }

    public Predicate[] having(CriteriaBuilder cb, Root<Property> root) {
        return new Predicate[0];
    }
    
    public void applySort(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<Property> root) {
        if (sortBy != null && !sortBy.isBlank()) {
            var sortField = root.get(sortBy);
            var orderClause = "DESC".equalsIgnoreCase(order) ? cb.desc(sortField) : cb.asc(sortField);
            cq.orderBy(orderClause);
        } else {
            
            cq.orderBy(cb.desc(root.get(Property_.id)));
        }
    }
}