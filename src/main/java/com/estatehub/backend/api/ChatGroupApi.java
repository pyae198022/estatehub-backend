package com.estatehub.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estatehub.backend.model.dto.Input.ChatMessageForm;
import com.estatehub.backend.model.dto.Output.ChatGroupItem;
import com.estatehub.backend.model.dto.Output.ChatMessageItem;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.service.ChatGroupService;
import com.estatehub.backend.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class ChatGroupApi {

	private final ChatGroupService chatGroupService;

	@PostMapping("/accept/{interestId}")
	public ModificationResult<Long> accept(@PathVariable Long interestId) {
		return chatGroupService.acceptGroup(interestId, SecurityUtils.getCurrentUserId());
	}

	@GetMapping("/mine")
	public List<ChatGroupItem> mine() {
		return chatGroupService.mine(SecurityUtils.getCurrentUserId());
	}

	@GetMapping("/{id}/messages")
	public List<ChatMessageItem> messages(@PathVariable Long id) {
		return chatGroupService.messages(id, SecurityUtils.getCurrentUserId(), SecurityUtils.isAdmin());
	}

	@PostMapping("/{id}/messages")
	public ModificationResult<Long> send(@PathVariable Long id, @RequestBody ChatMessageForm form) {
		return chatGroupService.sendMessage(id, SecurityUtils.getCurrentUserId(), form);
	}

	@GetMapping("/admin/all")
	public List<ChatGroupItem> adminAll() {
		return chatGroupService.adminAll();
	}

	@DeleteMapping("/admin/{id}/members/{userId}")
	public ModificationResult<Long> removeMember(@PathVariable Long id, @PathVariable Long userId) {
		return chatGroupService.removeMember(id, userId);
	}
}
