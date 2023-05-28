package com.boup.boup.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.boup.boup.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public Optional<User> findByUsername(String nick);
	public Optional<User> findByEmail(String email);
	
	@Query("select u.username from Boup_User u where u.username Like :username%")
	public List<String> findUsernames(String username);
}
