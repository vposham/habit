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
        FinderUtils finderUtils= new FinderUtils();
        String city = finderUtils.findCity(latitude,longitude);
        List<Recording> usersRecordings = recordsRepo.findByCityAndUserId(city, userId);
        List<Recording> publicRecordings = recordsRepo.findByCityAndIsPrivate(city, false);
        List<RecordingRespMeta> out = new ArrayList<RecordingRespMeta>(usersRecordings.size());

        for (Recording userRecord : usersRecordings) {
            RecordingRespMeta meta = new RecordingRespMeta();
            meta.setIsPrivate(userRecord.isPrivate);
            meta.setLatitude(userRecord.latitude);
            meta.setLongitude(userRecord.longitude);
            meta.setTags(userRecord.getTags());
            meta.setRecId(userRecord.recordingId);
            out.add(meta);
        }
        for (Recording record : publicRecordings) {
            RecordingRespMeta meta = new RecordingRespMeta();
            meta.setIsPrivate(record.isPrivate);
            meta.setLatitude(record.latitude);
            meta.setLongitude(record.longitude);
            meta.setTags(record.getTags());
            meta.setRecId(record.recordingId);
            out.add(meta);
        }
        return out;
    }

    public byte[] findRecordingByRecordingId(String recId) {
        Recording recording = recordsRepo.findRecordingByRecordingId(recId);
        return recording.getRecording();

    }
}
