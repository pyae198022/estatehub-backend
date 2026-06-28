package com.estatehub.backend.model;

import java.util.List;
import java.util.function.Function;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class BaseRepoImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepo<T, ID>{

	private EntityManager manager;
	
	public BaseRepoImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.manager = entityManager;
	}
	
	@Override
	public <R> List<R> search(Function<CriteriaBuilder, CriteriaQuery<R>> queryFunc) {
		var criteriaQuery = queryFunc.apply(manager.getCriteriaBuilder());
		var query = manager.createQuery(criteriaQuery);
		
		return query.getResultList();
	}

}
