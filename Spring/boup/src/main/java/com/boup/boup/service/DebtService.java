package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;

@Service
public interface DebtService {
	public Optional<Debt> insert(Debt d);
	public Optional<Debt> update(Debt d);
	public boolean delete(Integer id);
	public List<Debt> findAll();
	public List<Debt> findByUser(User u);
	public List<Debt> findByUserAndGroup(User u,Group g);
	public Optional<Debt> findById(Integer id);
	public Optional<Debt> addDebt(Debt d);
	public List<User> findGroupUsers(Group g);
	public List<Group> findUserGroups(User user);
	public List<Debt> findGroupDebts(Group group);
	public List<Debt> findUserDebts(User user);
}
