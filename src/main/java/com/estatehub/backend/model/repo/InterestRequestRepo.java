package com.estatehub.backend.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estatehub.backend.model.entity.InterestRequest;

public interface InterestRequestRepo extends JpaRepository<InterestRequest, Long> {

	List<InterestRequest> findByRequester_IdOrderByCreatedAtDesc(Long requesterId);

	Optional<InterestRequest> findByProperty_IdAndRequester_IdAndStatus(Long propertyId, Long requesterId, String status);

	List<InterestRequest> findByStatusOrderByCreatedAtDesc(String status);

	List<InterestRequest> findByStatusAndProperty_Owner_IdOrderByCreatedAtDesc(String status, Long ownerId);
}