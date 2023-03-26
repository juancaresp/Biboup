package com.boup.boup.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Spent {

	@Id
	@EqualsAndHashCode.Include
	@NonNull
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(nullable = false)
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	private User payer;
	
	@OneToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	private List<User> users;
	
	private Double quantity;
	
	private LocalDate date;
}
