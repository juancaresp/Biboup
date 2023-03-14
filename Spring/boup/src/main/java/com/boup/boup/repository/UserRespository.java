package com.boup.boup.repository;

import org.springframework.data.repository.CrudRepository;

import com.boup.boup.model.User;

public interface UserRespository extends CrudRepository<User, String> {

}
