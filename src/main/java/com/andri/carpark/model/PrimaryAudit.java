package com.andri.carpark.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "id"})
public class PrimaryAudit implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id",unique = true, nullable = false)
	private Integer id;

	@JsonIgnore
	@Column(name = "created_at")
	private Instant createdAt;

	@JsonIgnore
	@Column(name = "updated_at")
	private Instant updatedAt;

	public PrimaryAudit() {
		this.createdAt = Instant.now();
		this.updatedAt = null;
	}
}
