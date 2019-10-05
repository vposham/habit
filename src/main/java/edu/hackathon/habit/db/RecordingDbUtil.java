package edu.hackathon.habit.db;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordingDbUtil extends MongoRepository<Recording, String> {

    public Recording findByRecordingId(String recordingId);

    public List<Recording> findByCityAndUserId(String city, String userId);
    public List<Recording> findByCityAndIsPrivate(String city, boolean isPrivate);
}
