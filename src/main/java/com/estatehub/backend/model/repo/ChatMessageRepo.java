package com.estatehub.backend.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estatehub.backend.model.entity.ChatMessage;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findByGroup_IdOrderByCreatedAtAsc(Long groupId);

	Optional<ChatMessage> findTopByGroup_IdOrderByCreatedAtDesc(Long groupId);
}