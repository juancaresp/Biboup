package com.boup.boup.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
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
