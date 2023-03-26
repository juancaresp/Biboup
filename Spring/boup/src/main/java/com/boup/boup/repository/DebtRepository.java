package com.boup.boup.repository;

import org.springframework.data.repository.CrudRepository;

import com.boup.boup.model.Debt;
import com.boup.boup.model.DebtPK;

public interface DebtRepository extends CrudRepository<Debt, DebtPK>{

}
