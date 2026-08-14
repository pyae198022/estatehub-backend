package com.estatehub.backend.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estatehub.backend.model.dto.Input.InterestForm;
import com.estatehub.backend.model.dto.Output.InterestRequestItem;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.service.InterestRequestService;
import com.estatehub.backend.utils.SecurityUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/interests")
@RequiredArgsConstructor
public class InterestApi {

	private final InterestRequestService interestService;

	@PostMapping("/request")
	public ModificationResult<Long> request(@RequestBody InterestForm form) {
		return interestService.submit(form, SecurityUtils.getCurrentUserId());
	}

	@GetMapping("/mine")
	public List<InterestRequestItem> mine(@RequestParam(required = false) Long propertyId) {
		return interestService.mine(SecurityUtils.getCurrentUserId(), propertyId);
	}

	@GetMapping("/owner")
	public List<InterestRequestItem> approvedForOwner(@RequestParam(required = false) Long propertyId) {
		return interestService.approvedForOwner(SecurityUtils.getCurrentUserId(), propertyId);
	}

	@GetMapping("/admin/pending")
	public List<InterestRequestItem> pending() {
		return interestService.pending();
	}

	@PutMapping("/{id}/approve")
	public ModificationResult<Long> approve(@PathVariable Long id) {
		return interestService.decide(id, "APPROVED");
	}

	@PutMapping("/{id}/reject")
	public ModificationResult<Long> reject(@PathVariable Long id) {
		return interestService.decide(id, "REJECTED");
	}
}