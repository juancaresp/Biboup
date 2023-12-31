package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.boup.boup.model.Group;

@Service
public interface GroupService {
	public Optional<Group> insert(Group g);
	public Optional<Group> update(Group g);
	public boolean delete(Integer id);
	public List<Group> findAll();
	public Optional<Group> findByGroupName(String name);
	public Optional<Group> findById(Integer id);
}
