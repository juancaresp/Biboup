package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class UserServiceImp implements UserService{
	
	@Autowired GroupRepository groupR;
	@Autowired SpentRepository spentR;
	@Autowired UserRepository userR;
	@Autowired DebtRepository debtR;


	@Override
	public Optional<User> insert(User u) {
		Optional<User> op=Optional.empty();
		
		if (userR.findByUsername(u.getUsername()).isEmpty()) {
			op=Optional.of(userR.save(u));
		}
		
		return op;
	}

	@Override
	public Optional<User> update(User u) {
		Optional<User> op=Optional.empty();
		if (userR.existsById(u.getId())) {
			op=Optional.of(userR.save(u));
		}
		return op;
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
