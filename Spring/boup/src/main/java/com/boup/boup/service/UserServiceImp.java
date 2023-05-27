package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.dto.AddWallet;
import com.boup.boup.dto.UserReg;
import com.boup.boup.dto.UserUpd;
import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	GroupRepository groupR;
	@Autowired
	SpentRepository spentR;
	@Autowired
	UserRepository userR;
	@Autowired
	DebtRepository debtR;
	@Autowired
	GroupService groupS;

	@Override
	public Optional<User> insert(User u) {
		Optional<User> op = Optional.empty();

		if (userR.findById(u.getId()).isEmpty()) {
			op = Optional.of(userR.save(u));
		}

		return op;
	}
	
	@Override
	public Optional<User> update(User u) {
		Optional<User> op = Optional.empty();
		if (userR.findById(u.getId()).isPresent()) {
			op = Optional.of(userR.save(u));
		}
		return op;
	}

	@Override
	public boolean delete(Integer id) {
		boolean exit = false;

		if (userR.existsById(id)) {
			userR.deleteById(id);
			exit = true;
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
		User u = new User();
		Optional<User> op = Optional.empty();

		if (userR.findByUsername(reg.getUsername()).isEmpty() && userR.findByEmail(reg.getEmail()).isEmpty()) {
			u.setToken(reg.getToken());
			u.setUsername(reg.getUsername());
			u.setNameU(reg.getNameU());
			u.setEmail(reg.getEmail());
			u.setTelephone(reg.getTelephone());
			op = Optional.of(userR.save(u));
		}

		return op;
	}

	@Override
	public Optional<User> updateU(UserUpd reg, String email) {

		Optional<User> op = userR.findByEmail(email);
		if (op.isPresent()) {
			User u = op.get();
			Optional<User> user=userR.findByUsername(reg.getUsername());
			if (user.isEmpty()|| u.getId()==user.get().getId()) {
				u.setUsername(reg.getUsername());
				u.setNameU(reg.getNameU());
				u.setTelephone(reg.getTelephone());
				op = Optional.of(userR.save(u));
			}
		}else {
			op=Optional.empty();
		}

		return op;
	}

	@Override
	public Optional<User> addWallet(AddWallet add) {
		Optional<User> op= userR.findByUsername(add.getUsername());
		User u;
		if(op.isPresent()) {
			u=op.get();
			u.setWallet(u.getWallet()+add.getAmount());
			Optional.of(userR.save(u));
		}else {
			op=Optional.empty();
		}
		
		
		return op;
	}

}
