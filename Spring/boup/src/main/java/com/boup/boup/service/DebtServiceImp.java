package com.boup.boup.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class DebtServiceImp implements DebtService {

	@Autowired GroupRepository groupR;
	@Autowired SpentRepository spentR;
	@Autowired UserRepository userR;
	@Autowired DebtRepository debtR;
	
	@Override
	public Optional<Debt> insert(Debt d) {
		
		Optional<Debt> op=Optional.of(debtR.save(d));
		return op;
	}

	@Override
	public Optional<Debt> update(Debt d) {
		
		Optional<Debt> op=Optional.empty();
		if (debtR.existsById(d.getId())) {
			op=Optional.of(debtR.save(d));
		}
		return op;
	}

	@Override
	public boolean delete(Integer id) {
		
		boolean exit=false;
		if (debtR.existsById(id)) {
			debtR.deleteById(id);
			exit=true;
		}
		return exit;
	}

	@Override
	public List<Debt> findAll() {
		
		return debtR.findAll();
	}

	@Override
	public List<Debt> findByReceiver(User u) {
		
		return debtR.findByReceiver(u);
	}

	@Override
	public List<Debt> findByDebtor(User u) {
		
		return debtR.findByDebtor(u);
	}
	
	@Override
	public Optional<Debt> findById(Integer id) {
		
		return debtR.findById(id);
	}


}
