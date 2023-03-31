package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public boolean insert(Group g) {
		
		boolean exit=false;
		if (!groupR.existsById(g.getId())) {
			groupR.save(g);
			exit=true;
		}
		return exit;
	}

	@Override
	public boolean update(Group g) {
		boolean exit=false;
		if (groupR.existsById(g.getId())) {
			groupR.save(g);
			exit=true;
		}
		return exit;
	}

	@Override
	public boolean delete(Integer id) {
		boolean exit=false;
		if(groupR.existsById(id)) {
			groupR.deleteById(id);
			exit=true;
		}
		return exit;
	}

	@Override
	public List<Group> findAll() {
		// TODO Auto-generated method stub
		return (List<Group>) groupR.findAll();
	}

	@Override
	public Optional<Group> findByGroupName(String name) {
		// TODO Auto-generated method stub
		return groupR.findByGroupName(name);
	}

	@Override
	public Optional<Group> findById(Integer id) {
		// TODO Auto-generated method stub
		return groupR.findById(id);
	}

}
