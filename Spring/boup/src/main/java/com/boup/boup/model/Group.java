package com.boup.boup.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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

@Entity(name = "Boup_Group")
public class Group implements Serializable{

	private static final long serialVersionUID = 8655739355491443999L;

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@Column(length = 20)
	private String groupName;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "debt", 
        joinColumns = { @JoinColumn(name = "group") }, 
        inverseJoinColumns = { @JoinColumn(name = "user") }
    )
	private List<User> users;

	@Override
	public String toString() {
		return "Group [id=" + id + ", groupName=" + groupName + ", users=" + users.stream().map(u -> u.getNameU()) + "]";
	}
	
	
}
