package com.boup.boup.service;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class DebtServiceImp implements DebtService {

	@Autowired
	GroupRepository groupR;
	@Autowired
	SpentRepository spentR;
	@Autowired
	UserRepository userR;
	@Autowired
	DebtRepository debtR;

	@Override
	public Optional<Debt> insert(Debt d) {

		Optional<Debt> op = Optional.of(debtR.save(d));
		return op;
	}

	@Override
	public Optional<Debt> update(Debt d) {

		Optional<Debt> op = Optional.empty();
		if (debtR.existsById(d.getId())) {
			op = Optional.of(debtR.save(d));
		}
		return op;
	}

	@Override
	public boolean delete(Integer id) {

		boolean exit = false;
		if (debtR.existsById(id)) {
			debtR.deleteById(id);
			exit = true;
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
	public List<Debt> findByReceiverAndGroup(User u, Group g) {

		return debtR.findByReceiverAndDebtGroup(u,g);
	}

	@Override
	public List<Debt> findByDebtorAndGroup(User u, Group g) {

		return debtR.findByDebtorAndDebtGroup(u,g);
	}


	@Override
	public Optional<Debt> findById(Integer id) {

		return debtR.findById(id);
	}

	@Override
	public List<Debt> findUserDebts(Integer id) {
		// TODO Auto-generated method stub
		List<Debt> debts = new ArrayList<>();
		Optional<User> us = userR.findById(id);

		us.ifPresent(u -> {
			debts.addAll(findByDebtor(u));
			debts.addAll(findByReceiver(u));
		});

		return debts;
	}

	//El bonito
	/*
	@Override
	public Optional<Debt> addDebt(Debt d) {

		Optional<Debt> op = Optional.empty();
		
		if(userR.existsById(d.getDebtor().getId())&&userR.existsById(d.getReceiver().getId())) {
			//Caso en el ambos usuarios existen
			Debt debt;
			List<Debt> debts=findByDebtor(d.getDebtor());
			Optional<Debt> deb=debts.stream().filter(de -> de.getReceiver().equals(d.getReceiver())).findFirst();
			if(deb.isPresent()) {
				//Caso en el que se le suma la deuda a una ya existente
				debt=deb.get();
				debt.setAmount(debt.getAmount()+d.getAmount());
			}else {
				//se reduce una deuda ya existente
				debts=findByDebtor(d.getReceiver());
				deb=debts.stream().filter(de -> de.getReceiver().equals(d.getDebtor())).findFirst();
				if(deb.isPresent()) {
					debt=deb.get();
					if(debt.getAmount()-d.getAmount()<0) {
						debt.setDebtor(d.getDebtor());
						debt.setReceiver(d.getReceiver());
						debt.setAmount(d.getAmount()-debt.getAmount());
					}else {
						debt.setAmount(debt.getAmount()-d.getAmount());
					}
				}else {
					//El caso en el que no exista deuda entre ellos dos
					debt=d;
				}
			}
			
			//Se guarda la deuda
			op=Optional.of(debtR.save(debt));
		
		}
		return op;
	}
	*/
	@Override
	public Optional<Debt> addDebt(Debt d) {

		Optional<Debt> op = Optional.empty();
		
		if(userR.existsById(d.getDebtor().getId())&&userR.existsById(d.getReceiver().getId())) {
			
			//Caso en el ambos usuarios existen
			Debt debt;
			List<Debt> debts=findByDebtorAndGroup(d.getDebtor(),d.getDebtGroup());
			
			Optional<Debt> deb=debts.stream()
					.filter(de -> de.getReceiver().equals(d.getReceiver()))
					.findFirst();
			
			if(deb.isPresent()) {
				//Caso en el que se le suma la deuda a una ya existente
				debt=deb.get();
				debt.setAmount(debt.getAmount()+d.getAmount());
				
			}else {
				//se reduce una deuda ya existente
				
				debts=findByDebtorAndGroup(d.getReceiver(),d.getDebtGroup());
				deb=debts.stream()
						.filter(de -> de.getReceiver().equals(d.getDebtor()))
						.findFirst();
				
				if(deb.isPresent()) {
					debt=deb.get();
					
					if(debt.getAmount()-d.getAmount()<0) {
						debt.setDebtor(d.getDebtor());
						debt.setReceiver(d.getReceiver());
						debt.setAmount(d.getAmount()-debt.getAmount());
						
					}else {
						debt.setAmount(debt.getAmount()-d.getAmount());
					}
					
				}else {
					//El caso en el que no exista deuda entre ellos dos
					debt=d;
				}
			}
			
			//Se guarda la deuda
			op=Optional.of(debtR.save(debt));
		
		}
		return op;
	}
}
