package com.boup.boup.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.boup.boup.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	public Optional<User> findByUsername(String nick);

}
