package com.estatehub.backend.model.dto.Input;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.estatehub.backend.model.entity.Property;
import com.estatehub.backend.model.entity.Property_;

public record PropertySearch(
    String keyword,
    String township,
    String listingType,
    String propertyType,
    BigDecimal minPrice,
    BigDecimal maxPrice
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
            predicates.add(cb.equal(root.get(Property_.listingType), propertyType));
        }

        if (minPrice != null) {
            predicates.add(cb.ge(root.get(Property_.price), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(cb.le(root.get(Property_.price), maxPrice));
        }

        // Active Status Criteria
        predicates.add(cb.equal(root.get(Property_.status), "AVAILABLE"));

        return predicates.toArray(new Predicate[0]);
    }

    public Predicate[] having(CriteriaBuilder cb, Root<Property> root) {
        return new Predicate[0];
    }
}