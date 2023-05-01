package com.boup.boup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.boup.boup.model.Group;
import com.boup.boup.model.User;

public interface GroupRepository  extends JpaRepository<Group, Integer>{

	public Optional<Group> findByGroupName(String name);
	public List<Group> findByUsers(User u);
}
