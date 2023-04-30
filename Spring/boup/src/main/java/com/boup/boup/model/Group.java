package com.boup.boup.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

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

@Entity(name = "Boup_Group")
public class Group implements Serializable{
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Integer id;
	
	@Column(length = 20)
	private String groupName;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "group_User", 
        joinColumns = { @JoinColumn(name = "group_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
	private List<User> users;
	
	@OneToMany(mappedBy="group",fetch = FetchType.EAGER)
    private Set<Spent> spents;
	
	@OneToMany(mappedBy="debtGroup",fetch = FetchType.EAGER)
    private Set<Debt> debts;

	@Override
	public String toString() {
		return "Group [id=" + id + ", groupName=" + groupName + ", users=" + users.stream().map(u -> u.getNameU()) + ", spents=" + spents.stream().map(sp-> sp.getSpentName()) + ", debts="
				+ debts + "]";
	}
	
	
}
