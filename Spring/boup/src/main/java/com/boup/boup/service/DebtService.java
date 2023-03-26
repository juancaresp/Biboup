package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import com.boup.boup.model.Debt;
import com.boup.boup.model.DebtPK;

public interface DebtService {
	public boolean insert(Debt d);
	public boolean update(Debt d);
	public boolean delete(DebtPK id);
	public List<Debt> findAll();
	public Optional<Debt> findById(DebtPK id);
}
