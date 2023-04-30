package com.boup.boup.model;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
@Builder

@Entity
public class Debt implements Serializable{
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User receiver;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User debtor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Group debtGroup;
	
	private Double amount;

	@Override
	public String toString() {
		return "Debt [id=" + id + ", receiver=" + receiver.getNameU() + ", debtor=" + debtor.getNameU() + ", debtGroup=" + debtGroup.getGroupName()
				+ ", amount=" + amount + "]";
	}
	
}
