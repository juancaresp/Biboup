package com.boup.boup.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

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
	private Integer id;
	
	@EqualsAndHashCode.Include
	@ManyToOne(fetch = FetchType.EAGER)
	private User receiver;
	
	@EqualsAndHashCode.Include
	@ManyToOne(fetch = FetchType.EAGER)
	private User debtor;
	
	private Double amount;
}
