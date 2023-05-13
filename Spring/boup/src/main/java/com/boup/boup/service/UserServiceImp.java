package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.dto.UserReg;
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
	@Autowired GroupService groupS;


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
		
		if (userR.existsById(id)) {
			userR.deleteById(id);
			exit=true;
		}
		
		return exit;
	}

	@Override
	public List<User> findAll() {
		
		return (List<User>) userR.findAll();
	}

	@Override
	public Optional<User> findByNick(String nick) {
		
		return userR.findByUsername(nick);
	}

	@Override
	public Optional<User> findById(Integer id) {
		
		return userR.findById(id);
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return userR.findByEmail(email);
	}

	@Override
	public Optional<User> register(UserReg reg) {
		User u=new User();
		Optional<User> op=Optional.empty();
		
		if(userR.findByUsername(reg.getUsername()).isEmpty()) {
			u.setToken(reg.getToken());
			u.setUsername(reg.getUsername());
			u.setNameU(reg.getNameU());
			u.setEmail(reg.getEmail());
			u.setTelephone(reg.getTelephone());
			op= Optional.of(userR.save(u));
		}

		return op;
	}




}
