package com.boup.boup.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Entity(name = "Boup_Group")
public class Group {
	@Id
	@EqualsAndHashCode.Include
	@NonNull
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(nullable = false)
	private Integer id;
	
	@Column(length = 20)
	private String groupName;
	
	@OneToMany(fetch = FetchType.EAGER)
	private List<User> users;
}
