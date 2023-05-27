package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Spent;
import com.boup.boup.model.User;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class SpentServiceImp implements SpentService {

	@Autowired
	GroupRepository groupR;
	@Autowired
	SpentRepository spentR;
	@Autowired
	UserRepository userR;
	@Autowired
	DebtRepository debtR;
	@Autowired
	DebtService debtS;
	@Autowired
	GroupService groupS;
	
	@Override
	public Optional<Spent> insert(Spent s) {
		System.out.println(s);
		Optional<Spent> op = Optional.of(spentR.save(s));
		
		return op;
	}

	@Override
	public Optional<Spent> update(Spent s) {
		Optional<Spent> op = Optional.empty();

		if (!spentR.existsById(s.getId())) {
			op = Optional.of(spentR.save(s));
		}
		return op;
	}

	@Override
	public boolean delete(Integer id) {
		boolean exit = false;
		if (spentR.existsById(id)) {
			spentR.deleteById(id);
			exit = true;
		}
		return exit;
	}

	@Override
	public List<Spent> findAll() {
		// TODO Auto-generated method stub
		return (List<Spent>) spentR.findAll();
	}

	@Override
	public Optional<Spent> findById(Integer id) {
		// TODO Auto-generated method stub
		return spentR.findById(id);
	}

	@Override
	public List<Spent> findByGroup(Integer id) {
		// TODO Auto-generated method stub
		return spentR.findByGroupId(id);
	}
	
	@Override
	public Optional<Spent> addSpent(Spent spent) {
		
		Optional<Spent> spe =insert(spent);

		spe.ifPresent((sp) -> {
			Double part = sp.getQuantity() / (sp.getUsers().size() + 1);
			groupS.findById(sp.getGroup().getId()).ifPresent(g->
			{
				Debt d=debtS.findByUserAndGroup(sp.getPayer(), g).orElse(new Debt());
				debtR.save(d);
				d.setAmount(d.getAmount()+sp.getQuantity()-part);
				
				sp.getUsers().forEach(u->{
					Debt de=debtS.findByUserAndGroup(u, g).orElse(new Debt());
					de.setAmount(de.getAmount()-part);
					debtR.save(de);
				});
				
			});;
		});

		return spe;
	}
	
	@Override
	public void deleteUserSpent(Integer spentId, Integer userid) {
		// TODO Auto-generated method stub
		spentR.findById(spentId).ifPresent(s ->{
			User u= userR.findById(userid).orElse(new User());
			s.getUsers().remove(u);
			spentR.save(s);
		});;
		
	}

	@Override
	public void addUserSpent(Integer spentId, Integer userid) {
		// TODO Auto-generated method stub
		spentR.findById(spentId).ifPresent(s ->{
			User u= userR.findById(userid).orElse(new User());
			s.getUsers().add(u);
			spentR.save(s);
		});;
		
	}

	@Override
	public List<Spent> findByUser(String username) {
		return spentR.findByPayerUsernameOrUsersUsername(username, username);
	}

}
