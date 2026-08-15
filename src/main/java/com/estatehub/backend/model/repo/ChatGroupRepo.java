package com.estatehub.backend.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estatehub.backend.model.entity.ChatGroup;

public interface ChatGroupRepo extends JpaRepository<ChatGroup, Long> {

	Optional<ChatGroup> findByInterestRequest_Id(Long interestRequestId);

	List<ChatGroup> findAllByOrderByCreatedAtDesc();
}