package edu.hackathon.habit.model.db;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordingDbUtil extends MongoRepository<User, String> {

    public Recording get(String recordingId);

}
