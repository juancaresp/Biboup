package com.boup.boup.service;

import java.util.List;
import java.util.Optional;

import com.boup.boup.model.Group;

public interface GroupService {
	public boolean insert(Group g);
	public boolean update(Group g);
	public boolean delete(Integer id);
	public List<Group> findAll();
	public Optional<Group> findByGroupName(String name);
	public Optional<Group> findById(Integer id);
}
