package edu.hackathon.habit.db;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordingDbUtil extends MongoRepository<Recording, String> {

    public Recording findByRecordingId(String recordingId);

}
