package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.boup.boup.model.Debt;
import com.boup.boup.model.DebtPK;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

public class DebtServiceImp implements DebtService{

	@Autowired DebtRepository debtR;
	@Autowired GroupRepository groupR;
	@Autowired SpentRepository spentR;
	@Autowired UserRepository userR;
	
	@Override
	public boolean insert(Debt d) {
		boolean exit=false;
		if (!debtR.existsById(d.getIdDebt())) {
			debtR.save(d);
			exit=true;
		}
		return exit;
	}

	@Override
	public boolean update(Debt d) {
		// TODO Auto-generated method stub
		boolean exit=false;
		if (debtR.existsById(d.getIdDebt())) {
			debtR.save(d);
			exit=true;
		}
		return exit;
	}

	@Override
	public boolean delete(DebtPK id) {
		// TODO Auto-generated method stub
		boolean exit=false;
		if(debtR.existsById(id)) {
			debtR.deleteById(id);
			exit=true;
		}
		return exit;
	}

	@Override
	public List<Debt> findAll() {
		// TODO Auto-generated method stub
		return (List<Debt>) debtR.findAll();
	}

	@Override
	public Optional<Debt> findById(DebtPK id) {
		// TODO Auto-generated method stub
		return debtR.findById(id);
	}

}
