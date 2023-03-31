package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import com.boup.boup.model.Debt;

public interface DebtService {
	public boolean insert(Debt d);
	public boolean update(Debt d);
	public boolean delete(Integer id);
	public List<Debt> findAll();
	public Optional<Debt> findByReciever(String name);
	public Optional<Debt> findById(Integer id);
}
