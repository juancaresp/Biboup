package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.boup.boup.model.Spent;
import com.boup.boup.repository.GroupRepository;
import com.boup.boup.repository.SpentRepository;
import com.boup.boup.repository.UserRepository;

public class SpentServiceImp implements SpentService{

	@Autowired GroupRepository groupR;
	@Autowired SpentRepository spentR;
	@Autowired UserRepository userR;
	
	@Override
	public boolean insert(Spent s) {
		boolean exit=false;
		if (!spentR.existsById(s.getId())) {
			spentR.save(s);
			exit=true;
		}
		return exit;
	}
	@Override
	public boolean update(Spent s) {
		boolean exit=false;
		if (!spentR.existsById(s.getId())) {
			spentR.save(s);
			exit=true;
		}
		return exit;
	}
	@Override
	public boolean delete(Integer id) {
		boolean exit=false;
		if (!spentR.existsById(id)) {
			spentR.deleteById(id);
			exit=true;
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
}
