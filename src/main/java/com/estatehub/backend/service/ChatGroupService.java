package com.estatehub.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.dto.Input.ChatMessageForm;
import com.estatehub.backend.model.dto.Output.ChatGroupItem;
import com.estatehub.backend.model.dto.Output.ChatMessageItem;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.entity.ChatGroup;
import com.estatehub.backend.model.entity.ChatGroupMember;
import com.estatehub.backend.model.entity.ChatMessage;
import com.estatehub.backend.model.entity.InterestRequest;
import com.estatehub.backend.model.repo.ChatGroupMemberRepo;
import com.estatehub.backend.model.repo.ChatGroupRepo;
import com.estatehub.backend.model.repo.ChatMessageRepo;
import com.estatehub.backend.model.repo.InterestRequestRepo;
import com.estatehub.backend.model.repo.UserProfileRepo;
import com.estatehub.backend.utils.AppBussinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatGroupService {

	private final ChatGroupRepo groupRepo;
	private final ChatGroupMemberRepo memberRepo;
	private final ChatMessageRepo messageRepo;
	private final InterestRequestRepo interestRepo;
	private final UserProfileRepo profileRepo;

	@Transactional
	public ModificationResult<Long> acceptGroup(Long interestId, Long sellerId) {
		var interest = interestRepo.findById(interestId)
				.orElseThrow(() -> new AppBussinessException("Interest request not found"));

		if (!"APPROVED".equals(interest.getStatus())) {
			throw new AppBussinessException("Only admin-approved buyers can be accepted into a group");
		}
		if (!interest.getProperty().getOwner().getId().equals(sellerId)) {
			throw new AppBussinessException("You can only accept buyers for your own listing");
		}
		groupRepo.findByInterestRequest_Id(interestId).ifPresent(existing -> {
			throw new AppBussinessException("A chat group already exists for this interest request");
		});

		var group = new ChatGroup();
		group.setInterestRequest(interest);
		group.setStatus("ACTIVE");
		group = groupRepo.save(group);

		saveMember(group, interest.getRequester(), "BUYER");
		saveMember(group, interest.getProperty().getOwner(), "SELLER");

		interest.setStatus("ACCEPTED");
		return ModificationResult.success(group.getId());
	}

	public List<ChatGroupItem> mine(Long userId) {
		return memberRepo.findByUser_IdAndRemovedAtIsNull(userId).stream()
				.map(member -> toGroupItem(member.getGroup()))
				.distinct()
				.toList();
	}

	public List<ChatGroupItem> adminAll() {
		return groupRepo.findAllByOrderByCreatedAtDesc().stream()
				.map(this::toGroupItem)
				.toList();
	}

	public List<ChatMessageItem> messages(Long groupId, Long userId, boolean admin) {
		var group = groupRepo.findById(groupId)
				.orElseThrow(() -> new AppBussinessException("Chat group not found"));

		if (!admin && !isActiveMember(groupId, userId)) {
			throw new AppBussinessException("You are not a member of this chat group");
		}

		return messageRepo.findByGroup_IdOrderByCreatedAtAsc(groupId).stream()
				.map(message -> ChatMessageItem.from(message, nameOf(message.getSender().getId())))
				.toList();
	}

	@Transactional
	public ModificationResult<Long> sendMessage(Long groupId, Long senderId, ChatMessageForm form) {
		if (!isActiveMember(groupId, senderId)) {
			throw new AppBussinessException("You are not an active member of this chat group");
		}

		var group = groupRepo.findById(groupId)
				.orElseThrow(() -> new AppBussinessException("Chat group not found"));
		if (!"ACTIVE".equals(group.getStatus())) {
			throw new AppBussinessException("This chat group is closed");
		}

		var sender = memberRepo.findByGroup_IdAndUser_Id(groupId, senderId).orElseThrow().getUser();

		var message = new ChatMessage();
		message.setGroup(group);
		message.setSender(sender);
		message.setContent(form.content().trim());

		return ModificationResult.success(messageRepo.save(message).getId());
	}

	@Transactional
	public ModificationResult<Long> removeMember(Long groupId, Long memberUserId) {
		var membership = memberRepo.findByGroup_IdAndUser_Id(groupId, memberUserId)
				.orElseThrow(() -> new AppBussinessException("User is not a member of this chat group"));

		membership.setRemovedAt(LocalDateTime.now());

		var activeLeft = memberRepo.findByGroup_IdOrderByJoinedAt(groupId).stream()
				.filter(member -> member.getRemovedAt() == null)
				.count();
		if (activeLeft == 0) {
			var group = membership.getGroup();
			group.setStatus("CLOSED");
		}

		return ModificationResult.success(groupId);
	}

	private void saveMember(ChatGroup group, com.estatehub.backend.model.entity.User user, String role) {
		var member = new ChatGroupMember();
		member.setGroup(group);
		member.setUser(user);
		member.setRole(role);
		memberRepo.save(member);
	}

	private boolean isActiveMember(Long groupId, Long userId) {
		return memberRepo.findByGroup_IdAndUser_Id(groupId, userId)
				.map(member -> member.getRemovedAt() == null)
				.orElse(false);
	}

	private ChatGroupItem toGroupItem(ChatGroup group) {
		var interest = group.getInterestRequest();
		var buyer = interest.getRequester();
		var seller = interest.getProperty().getOwner();
		var lastMessage = messageRepo.findTopByGroup_IdOrderByCreatedAtDesc(group.getId()).orElse(null);
		return ChatGroupItem.from(group,
				nameOf(buyer.getId()),
				nameOf(seller.getId()),
				lastMessage != null ? lastMessage.getContent() : null,
				lastMessage != null ? lastMessage.getCreatedAt() : null);
	}

	private String nameOf(Long userId) {
		return profileRepo.findByUserId(userId)
				.map(profile -> profile.getFullName())
				.orElse(null);
	}
}
