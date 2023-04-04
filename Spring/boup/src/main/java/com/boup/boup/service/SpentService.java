package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boup.boup.model.Spent;
import com.boup.boup.model.User;

@Service
public interface SpentService {
	public Optional<Spent> insert(Spent s);
	public Optional<Spent> update(Spent s);
	public boolean delete(Integer id);
	public List<Spent> findAll();
	public Optional<Spent> findById(Integer id);
}
