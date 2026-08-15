package com.estatehub.backend.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Entity
@Data
@Table(name = "chat_group_member", uniqueConstraints = @UniqueConstraint(columnNames = { "group_id", "user_id" }))
public class ChatGroupMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "group_id", nullable = false)
	private ChatGroup group;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false)
	private String role; // BUYER, SELLER

	@Column(name = "removed_at")
	private LocalDateTime removedAt;

	@Column(name = "joined_at", updatable = false)
	private LocalDateTime joinedAt;

	@PrePersist
	protected void onCreate() {
		this.joinedAt = LocalDateTime.now();
	}
}