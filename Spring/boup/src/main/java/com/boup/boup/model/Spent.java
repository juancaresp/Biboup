package com.boup.boup.model;

import java.io.Serializable;
import java.time.LocalDate;
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
import javax.persistence.ManyToOne;

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
public class Spent implements Serializable{


	private static final long serialVersionUID = 8084600636335782429L;

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 30)
	private String spentName;
	
	@Column(length = 250)
	private String spentDesc;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private User payer;
	
	@ManyToMany
    @JoinTable(
        name = "spent_User", 
        joinColumns = { @JoinColumn(name = "spent_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
	private List<User> users;
	
	private Double quantity;
	
	private LocalDate date;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Group group;

	@Override
	public String toString() {
		return "Spent [id=" + id + ", spentName=" + spentName + ", payer=" + payer.getNameU() + ", users=" + users.stream().map(u-> u.getNameU()) + ", quantity="
				+ quantity + ", date=" + date + ", group=" + group.getGroupName() + "]";
	}
	
}
