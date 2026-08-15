package com.estatehub.backend.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.estatehub.backend.model.entity.ChatGroupMember;

public interface ChatGroupMemberRepo extends JpaRepository<ChatGroupMember, Long> {

	Optional<ChatGroupMember> findByGroup_IdAndUser_Id(Long groupId, Long userId);

	List<ChatGroupMember> findByUser_IdAndRemovedAtIsNull(Long userId);

	List<ChatGroupMember> findByGroup_IdOrderByJoinedAt(Long groupId);
}