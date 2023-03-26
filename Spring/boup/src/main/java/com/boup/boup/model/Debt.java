package com.boup.boup.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
@Builder

@Entity
public class Debt {
	@Id
	@EqualsAndHashCode.Include
	@NonNull
	@Column(nullable = false)
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private DebtPK idDebt;
	
	private Double amount;
}
