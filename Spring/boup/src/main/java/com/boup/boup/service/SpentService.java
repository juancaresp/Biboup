package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boup.boup.model.Spent;

@Service
public interface SpentService {
	public Optional<Spent> insert(Spent s);
	public Optional<Spent> update(Spent s);
	public boolean delete(Integer id);
	
	public List<Spent> findAll();
	public Optional<Spent> findById(Integer id);
	public List<Spent> findByGroup(Integer id);
	
	public Optional<Spent> addSpent(Spent spent);
	public void deleteUserSpent(Integer spentId, Integer userid);
	public void addUserSpent(Integer spentId, Integer userid);
	public List<Spent> findByUser(String username);
	public boolean deleteSpent(int id);
	public Optional<Spent> updateSpent(Spent spent);
	
}
