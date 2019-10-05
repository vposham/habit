package edu.hackathon.habit.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDbUtil extends MongoRepository<User, String> {

    public User findByUserId(String userId);

    public User findByUsername(String username);


}