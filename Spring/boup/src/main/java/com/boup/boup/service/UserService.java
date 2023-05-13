package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boup.boup.dto.UserReg;
import com.boup.boup.model.User;

@Service
public interface UserService {

	public Optional<User> insert(User u);
	public Optional<User> update(User u);
	public boolean delete(Integer id);
	public List<User> findAll();
	public Optional<User> findByNick(String nick);
	public Optional<User> findById(Integer id);
	public Optional<User> findByEmail(String email);
	public Optional<User> register(UserReg reg);
	
}
