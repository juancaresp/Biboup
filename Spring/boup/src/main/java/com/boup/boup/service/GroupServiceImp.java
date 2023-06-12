package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.Debt;
import com.boup.boup.model.Group;
import com.boup.boup.repository.DebtRepository;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

@Service
public class GroupServiceImp implements GroupService{
	

	@Autowired GroupRepository groupR;
	@Autowired SpentRepository spentR;
	@Autowired UserRepository userR;
	@Autowired DebtRepository debtR;


	@Override
	public Optional<Group> insert(Group g) {
		
		return Optional.of(groupR.save(g));
	}

	@Override
	public Optional<Group> update(Group g) {
		Optional<Group> op=Optional.empty();
		
		if (groupR.existsById(g.getId())) {
			op=Optional.of(groupR.save(g));
		}
		
		return op;
	}

	@Override
	public boolean delete(Integer id) {
		boolean exit=false;
		Optional<Group> opGroup=groupR.findById(id);
		
		if(opGroup.isPresent()) {
			Group g=opGroup.get();
			List<Debt> debts=debtR.findByGroup(g);
			boolean removable=true;
			
			for(int i=0;i<debts.size()&&removable;i++) {
				if(debts.get(i).getAmount()!=0)
					removable=false;
			}
			
			if(removable) {
				groupR.deleteById(id);
				exit=true;
			}
		}
		return exit;
	}

	@Override
	public List<Group> findAll() {
		return groupR.findAll();
	}

	@Override
	public Optional<Group> findByGroupName(String name) {
		return groupR.findByGroupName(name);
	}

	@Override
	public Optional<Group> findById(Integer groupID) {
		return groupR.findById(groupID);
	}

}
