package edu.hackathon.habit.model.db;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Recording {

    @Id
    public String recordingId;

    public String userId;

    public byte[] recording;

    public String transcript;

    public List<String> tags;

    public boolean isPrivate;

    public String latitude;

    public String longitude;

    public String city;

    public String timeStamp;

    public boolean isComplaint;

    public String title;

    public byte[] image;

    public Recording(String recordingId, String userId, byte[] recording, String transcript, List<String> tags, boolean isPrivate, String latitude, String longitude, String city, String timeStamp, boolean isComplaint, String title, byte[] image) {
        this.recordingId = recordingId;
        this.userId = userId;
        this.recording = recording;
        this.transcript = transcript;
        this.tags = tags;
        this.isPrivate = isPrivate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.timeStamp = timeStamp;
        this.isComplaint = isComplaint;
        this.title = title;
        this.image = image;
    }

    public String getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(String recordingId) {
        this.recordingId = recordingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getRecording() {
        return recording;
    }

    public void setRecording(byte[] recording) {
        this.recording = recording;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isComplaint() {
        return isComplaint;
    }

    public void setComplaint(boolean complaint) {
        isComplaint = complaint;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
