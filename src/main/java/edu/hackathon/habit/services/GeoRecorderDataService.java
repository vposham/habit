package edu.hackathon.habit.services;

import edu.hackathon.habit.db.Recording;
import edu.hackathon.habit.db.RecordingDbUtil;
import edu.hackathon.habit.model.RecordingRespMeta;
import edu.hackathon.habit.util.FinderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoRecorderDataService {

    @Autowired
    RecordingDbUtil recordsRepo;

    public List<RecordingRespMeta> getRecordingByCoordinate(String userId, String latitude, String longitude) {
        FinderUtils finderUtils = new FinderUtils();
        String city = finderUtils.findCity(latitude, longitude);
        List<Recording> usersRecordings = recordsRepo.findByCityAndUserId(city, userId);
        List<Recording> publicRecordings = recordsRepo.findByCityAndIsPrivate(city, false);
        List<RecordingRespMeta> out = new ArrayList<>(usersRecordings.size());
        addData(usersRecordings, out);
        addData(publicRecordings, out);
        return out;
    }

    public byte[] findRecordingByRecordingId(String recId) {
        Recording recording = recordsRepo.findRecordingByRecordingId(recId);
        return recording.getRecording();
    }

    private void addData(List<Recording> recoding, List<RecordingRespMeta> out) {
        for (Recording userRecord : recoding) {
            RecordingRespMeta meta = new RecordingRespMeta();
            meta.setIsPrivate(userRecord.isPrivate);
            meta.setLatitude(userRecord.latitude);
            meta.setLongitude(userRecord.longitude);
            meta.setTags(userRecord.getTags());
            meta.setRecId(userRecord.recordingId);
            meta.setTranscript(userRecord.transcript);
            out.add(meta);
        }
    }
}
