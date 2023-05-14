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
	public List<Debt> findByUser(User u) {

		return debtR.findByUser(u);
	}

	
	@Override
	public List<Debt> findByUserAndGroup(User u, Group g) {

		return debtR.findByUserAndGroup(u,g);
	}

	@Override
	public Optional<Debt> findById(Integer id) {

		return debtR.findById(id);
	}


	@Override
	public Optional<Debt> addDebt(Debt d) {
		
		return Optional.empty();
	}

	@Override
	public List<User> findGroupUsers(Group g) {
		
		List<Debt> debts=debtR.findByGroup(g);
		List<User> users=new ArrayList<>();
		debts.forEach(d-> users.add(d.getUser()));
		
		return users;
	}

	@Override
	public List<Group> findUserGroups(User user) {
		
		List<Debt> debts=debtR.findByUser(user);
		List<Group> groups=new ArrayList<>();
		debts.forEach(d-> groups.add(d.getGroup()));
		
		return groups;
	}

	@Override
	public List<Debt> findGroupDebts(Group group) {
		
		return debtR.findByGroup(group);
	}

	@Override
	public List<Debt> findUserDebts(User user) {
		// TODO Auto-generated method stub
		return debtR.findByUser(user);
	}

	/*
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
	}*/
}
