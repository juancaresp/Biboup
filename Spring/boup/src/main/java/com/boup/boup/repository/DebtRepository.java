package com.boup.boup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boup.boup.model.Debt;

public interface DebtRepository extends JpaRepository<Debt, Integer> {

}
