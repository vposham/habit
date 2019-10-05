package edu.hackathon.habit.model.db;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserDbUtil extends MongoRepository<User, String> {

    public User findByFirstName(String userId);

    public List<User> findByUsername(String username);

}