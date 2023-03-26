package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

public class UserServiceImp implements UserService{
	
	@Autowired DebtRepository debtR;
	@Autowired GroupRepository groupR;
	@Autowired SpentRepository spentR;
	@Autowired UserRepository userR;

	@Override
	public boolean insert(User u) {
		boolean exit=false;
		if (!userR.existsById(u.getId())) {
			userR.save(u);
			exit=true;
		}
		return exit;
	}

	@Override
	public boolean update(User u) {
		boolean exit=false;
		if (userR.existsById(u.getId())) {
			userR.save(u);
			exit=true;
		}
		return exit;
	}

	@Override
	public boolean delete(Integer id) {
		boolean exit=false;
		if (!userR.existsById(id)) {
			userR.deleteById(id);
			exit=true;
		}
		return exit;
	}

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return (List<User>) userR.findAll();
	}

	@Override
	public Optional<User> findByNick(String nick) {
		// TODO Auto-generated method stub
		return userR.findByUsername(nick);
	}

	@Override
	public Optional<User> findById(Integer id) {
		// TODO Auto-generated method stub
		return userR.findById(id);
	}

}
