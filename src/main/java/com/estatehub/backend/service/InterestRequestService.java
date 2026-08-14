package com.estatehub.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estatehub.backend.model.dto.Input.InterestForm;
import com.estatehub.backend.model.dto.Output.InterestRequestItem;
import com.estatehub.backend.model.dto.Output.ModificationResult;
import com.estatehub.backend.model.entity.InterestRequest;
import com.estatehub.backend.model.repo.InterestRequestRepo;
import com.estatehub.backend.model.repo.PropertyRepo;
import com.estatehub.backend.model.repo.UserProfileRepo;
import com.estatehub.backend.model.repo.UserRepo;
import com.estatehub.backend.utils.AppBussinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestRequestService {

	private final InterestRequestRepo interestRepo;
	private final PropertyRepo propertyRepo;
	private final UserRepo userRepo;
	private final UserProfileRepo profileRepo;

	@Transactional
	public ModificationResult<Long> submit(InterestForm form, Long requesterId) {
		var property = propertyRepo.findById(form.propertyId())
				.orElseThrow(() -> new AppBussinessException("Property not found"));

		var requester = userRepo.findById(requesterId)
				.orElseThrow(() -> new AppBussinessException("User not found"));

		interestRepo.findByProperty_IdAndRequester_IdAndStatus(form.propertyId(), requesterId, "PENDING")
				.ifPresent(existing -> {
					throw new AppBussinessException("You already have a pending interest request for this property");
				});

		var request = new InterestRequest();
		request.setProperty(property);
		request.setRequester(requester);
		request.setMessage(form.message());
		request.setStatus("PENDING");

		return ModificationResult.success(interestRepo.save(request).getId());
	}

	public List<InterestRequestItem> mine(Long requesterId, Long propertyId) {
		var requests = interestRepo.findByRequester_IdOrderByCreatedAtDesc(requesterId);

		return requests.stream()
				.filter(request -> propertyId == null || request.getProperty().getId().equals(propertyId))
				.map(request -> toItem(request))
				.toList();
	}

	public List<InterestRequestItem> pending() {
		return interestRepo.findByStatusOrderByCreatedAtDesc("PENDING").stream()
				.map(this::toItem)
				.toList();
	}

	public List<InterestRequestItem> approvedForOwner(Long ownerId, Long propertyId) {
		return interestRepo.findByStatusAndProperty_Owner_IdOrderByCreatedAtDesc("APPROVED", ownerId).stream()
				.filter(request -> propertyId == null || request.getProperty().getId().equals(propertyId))
				.map(this::toItem)
				.toList();
	}

	@Transactional
	public ModificationResult<Long> decide(Long id, String newStatus) {
		var request = interestRepo.findById(id)
				.orElseThrow(() -> new AppBussinessException("Interest request not found"));

		request.setStatus(newStatus);
		return new ModificationResult<>(true, id,
				"Interest request %d has been %s".formatted(id, newStatus.equals("APPROVED") ? "approved" : "rejected"));
	}

	private InterestRequestItem toItem(InterestRequest request) {
		var requester = request.getRequester();
		var requesterName = requester != null
				? profileRepo.findByUserId(requester.getId())
						.map(profile -> profile.getFullName())
						.orElse(null)
				: null;
		return InterestRequestItem.from(request, requesterName);
	}
}