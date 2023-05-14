package com.boup.boup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;

public interface DebtRepository extends JpaRepository<Debt, Integer> {

	public List<Debt> findByUser(User u);
	public List<Debt> findByUserAndGroup(User u,Group g);
	public List<Debt> findByGroup(Group g);
}
