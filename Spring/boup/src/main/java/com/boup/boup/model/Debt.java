package com.boup.boup.model;


import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder

@Entity
public class Debt implements Serializable{

	private static final long serialVersionUID = 1867490433167988556L;

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Group group;
	
	private Double amount;

	@Override
	public String toString() {
		return "Debt [id=" + id + ", receiver=" + user + ", debtGroup=" + group.getGroupName()
				+ ", amount=" + amount + "]";
	}
	
}
