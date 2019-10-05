package edu.hackathon.habit.services;

import edu.hackathon.habit.db.Recording;
import edu.hackathon.habit.db.RecordingDbUtil;
import edu.hackathon.habit.model.RecordingRespMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeoRecorderDataService {

    @Autowired
    RecordingDbUtil recordsRepo;

    public List<RecordingRespMeta> getRecordingByCoordinate(String userId, String latitude, String longitude) {
        String city = "Boca Raton";
        // String city = getCityFromCoordinates(latitude, longitude)
        List<Recording> usersRecordings = recordsRepo.findByCityAndUserId(city, userId);

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
        return out;
    }
}
