package com.boup.boup.model;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

@Entity(name = "Boup_User")
public class User {

	@Id
	@EqualsAndHashCode.Include
	@NonNull
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@Column(length = 30)
	private String token;
	
	@Column(length = 20)
	private String username;
	
	@Column(length = 20)
	private String nameU;
	
	@Column(length = 30)
	private String email;
	
	@Column(length = 100)
	private String password;
	
	@Column(length = 9)
	private String telephone;
	
	private Double wallet;
	
	@ManyToMany(mappedBy = "users")
	private Set<Group> groups;
	
	@ManyToMany(mappedBy = "users")
	private Set<Spent> spents;
	
}
