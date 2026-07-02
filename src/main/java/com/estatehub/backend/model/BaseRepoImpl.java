package com.estatehub.backend.model;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.estatehub.backend.model.dto.Input.PropertySearch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class BaseRepoImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepo<T, ID>{

	private EntityManager entityManager;
	
	public BaseRepoImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}
	
	@Override
	public <R> List<R> search(Function<CriteriaBuilder, CriteriaQuery<R>> queryFunc, PropertySearch search) {
	    var cb = entityManager.getCriteriaBuilder();
	    var cq = queryFunc.apply(cb);
	    var query = entityManager.createQuery(cq);
	    
	    int pageSize = search.size(); 
	    int firstResult = search.page() * pageSize;
	    
	    query.setFirstResult(firstResult);
	    query.setMaxResults(pageSize);
	    
	    return query.getResultList();
	}

}
