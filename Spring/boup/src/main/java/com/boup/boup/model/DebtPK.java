package com.boup.boup.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder

@Entity
public class DebtPK {
	
	@EqualsAndHashCode.Include
	@ManyToOne(fetch = FetchType.EAGER)
	private User reciver;
	
	@EqualsAndHashCode.Include
	@ManyToOne(fetch = FetchType.EAGER)
	private User debtor;
}
