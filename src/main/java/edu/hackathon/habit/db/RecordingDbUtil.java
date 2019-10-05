package edu.hackathon.habit.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordingDbUtil extends MongoRepository<Recording, String> {

    public Recording findByRecordingId(String recordingId);

}
