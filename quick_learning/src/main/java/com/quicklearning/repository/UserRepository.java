package com.quicklearning.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.quicklearning.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{


}
