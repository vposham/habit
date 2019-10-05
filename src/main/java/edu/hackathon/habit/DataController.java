package edu.hackathon.habit;

import edu.hackathon.habit.db.Recording;
import edu.hackathon.habit.db.RecordingDbUtil;
import edu.hackathon.habit.db.User;
import edu.hackathon.habit.db.UserDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DataController {

    @Autowired
    RecordingDbUtil recordingsRepository;

    @Autowired
    UserDbUtil usersRepository;

    @PostMapping
    public Recording createRecording(@RequestBody Recording recording) {
        recordingsRepository.save(recording);
        return recording;
    }

    @GetMapping
    public List listRecordings() {
        return recordingsRepository.findAll();
    }

    @PutMapping("/{recordingId}")
    public Recording updateRecording(@RequestBody Recording recording, @PathVariable String recordingId) {
        recording.setRecordingId(recordingId);
        recordingsRepository.save(recording);
        return recording;
    }

    @DeleteMapping("/{recordingId}")
    public String deleteRecording(@PathVariable String recordingId) {
        recordingsRepository.deleteById(recordingId);
        return recordingId;
    }


    @PostMapping
    public User createUser(@RequestBody User user) {
        usersRepository.save(user);
        return user;
    }

    @GetMapping
    public List listUsers() {
        return usersRepository.findAll();
    }

    @PutMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable String userId) {
        user.setUserId(userId);
        usersRepository.save(user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        usersRepository.deleteById(userId);
        return userId;
    }
}
