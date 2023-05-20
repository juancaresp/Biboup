package com.boup.boup.model;


import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Entity(name = "Boup_User")
public class User  implements Serializable {

	private static final long serialVersionUID = -1311989204087085838L;

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 400)
	private String token;
	
	@Column(length = 20,unique = true)
	private String username;
	
	@Column(length = 20)
	private String nameU;
	
	@Column(length = 30,unique = true)
	private String email;
	
	@Column(length = 9)
	private String telephone;
	
	private Double wallet;
	
	@Override
	public String toString() {
		return "User [id=" + id + ", token=" + token + ", username=" + username + ", nameU=" + nameU + ", email="
				+ email + ", telephone=" + telephone + ", wallet=" + wallet + "]";
	}
	
}
