package com.boup.boup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;

public interface DebtRepository extends JpaRepository<Debt, Integer> {

	public List<Debt> findByReceiver(User u);
	public List<Debt> findByDebtor(User u);
	public List<Debt> findByReceiverAndDebtGroup(User u,Group g);
	public List<Debt> findByDebtorAndDebtGroup(User u, Group g);
	public List<Debt> findByDebtGroup(Group g);
}
