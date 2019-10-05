package edu.hackathon.habit.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface UserDbUtil extends MongoRepository<User, String> {

    public User findByUserId(String userId);

    public List<User> findByUsername(String username);

}