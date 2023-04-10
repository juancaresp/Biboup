package com.boup.boup.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boup.boup.model.Spent;

public interface SpentRepository extends JpaRepository<Spent, Integer>{

	public List<Spent> findByGroupId(Integer id);

}
