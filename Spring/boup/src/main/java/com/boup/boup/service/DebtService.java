package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.User;

@Service
public interface DebtService {
	public Optional<Debt> insert(Debt d);
	public Optional<Debt> update(Debt d);
	public boolean delete(Integer id);
	public List<Debt> findAll();
	public List<Debt> findByReceiver(User u);
	public List<Debt> findByDebtor(User u);
	public Optional<Debt> findById(Integer id);
}
