package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import com.boup.boup.model.User;

public interface UserService {

	public boolean insert(User u);
	public boolean update(User u);
	public boolean delete(Integer id);
	public List<User> findAll();
	public Optional<User> findByNick(String nick);
	public Optional<User> findById(Integer id);
}
