package com.boup.boup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boup.boup.model.Group;

public interface GroupRepository  extends JpaRepository<Group, Integer>{

	public Optional<Group> findByGroupName(String name);
}
