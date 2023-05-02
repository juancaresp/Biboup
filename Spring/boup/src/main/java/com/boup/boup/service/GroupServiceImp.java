package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boup.boup.model.Group;
import com.boup.boup.model.User;
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
		
		Optional<Group> op=Optional.of(groupR.save(g));
		
		return op;
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
	public Optional<Group> findById(Integer groupID) {
		// TODO Auto-generated method stub
		return groupR.findById(groupID);
	}
	@Override
	public Optional<Group> addUserGroup(Integer groupID, Integer userid) {
		Optional<User> op=userR.findById(userid);
		Optional<Group> g= Optional.empty();
		
		if(op.isPresent()) {
			User u=op.get();
			g=findById(groupID);
			if(g.isPresent()){
				Group gr=g.get();
				gr.getUsers().add(u);
				g=Optional.of(gr);
				System.out.println(gr.getGroupName());
				groupR.save(gr);
			}
		}
		
		return g;
	}
	
	@Override
	public Optional<Group> deleteUserGroup(Integer groupid, Integer userid) {
		Optional<User> op=userR.findById(userid);
		Optional<Group> g= Optional.empty();
		
		if(op.isPresent()) {
			User u=op.get();
			g=findById(groupid);
			if(g.isPresent()){
				Group gr=g.get();
				gr.getUsers().remove(u);
				g=Optional.of(gr);
				groupR.save(gr);
			}
		}
		
		return g;
	}

	@Override
	public List<Group> findByUser(User u) {
		// TODO Auto-generated method stub
		return groupR.findByUsers(u);
	}


}
