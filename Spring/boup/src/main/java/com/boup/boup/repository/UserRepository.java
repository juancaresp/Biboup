package com.boup.boup.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boup.boup.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public Optional<User> findByUsername(String nick);

}
