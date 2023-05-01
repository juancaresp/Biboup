package com.boup.boup.model;


import java.io.Serializable;
import java.util.List;
import java.util.Set;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

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

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@Column(length = 30)
	private String token;
	
	@Column(length = 20,unique = true)
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
	
	@Override
	public String toString() {
		return "User [id=" + id + ", token=" + token + ", username=" + username + ", nameU=" + nameU + ", email="
				+ email + ", password=" + password + ", telephone=" + telephone + ", wallet=" + wallet + "]";
	}
	
}
