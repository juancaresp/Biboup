package com.boup.boup.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.boup.boup.model.Group;

public interface GroupRepository  extends CrudRepository<Group, Integer>{

	public Optional<Group> findByGroupName(String name);
}
