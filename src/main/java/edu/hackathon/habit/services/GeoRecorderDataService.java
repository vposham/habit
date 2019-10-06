package edu.hackathon.habit.services;

import edu.hackathon.habit.db.Recording;
import edu.hackathon.habit.db.RecordingDbUtil;
import edu.hackathon.habit.model.DownloadResponse;
import edu.hackathon.habit.model.RecordingRespMeta;
import edu.hackathon.habit.util.FinderUtils;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
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
        addData(usersRecordings,out);
        addData(publicRecordings,out);
        return out;
    }

    public DownloadResponse findRecordingByRecordingId(String recId) throws IOException {
        Recording recording = recordsRepo.findRecordingByRecordingId(recId);
        String  mediaType = detectDocTypeUsingDetector(new ByteArrayInputStream(recording.getRecording()));
        DownloadResponse downloadResponse = new DownloadResponse();
        downloadResponse.setRecording(recording.getRecording());
        downloadResponse.setTranscript(recording.getTranscript());
        downloadResponse.setMediaType(mediaType);
        return downloadResponse;
    }

    private void addData(List<Recording> recoding, List<RecordingRespMeta> out){
        for (Recording userRecord : recoding) {
            RecordingRespMeta meta = new RecordingRespMeta();
            meta.setIsPrivate(userRecord.isPrivate);
            meta.setLatitude(userRecord.latitude);
            meta.setLongitude(userRecord.longitude);
            meta.setTags(userRecord.getTags());
            meta.setRecId(userRecord.recordingId);
            out.add(meta);
        }
    }

    public static String detectDocTypeUsingDetector(InputStream stream)
            throws IOException {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

       MediaType mediaType = detector.detect(stream, metadata);
        //InputStream is = new ByteArrayInputStream(IOUtils.toByteArray(stream));
      //  String mimeType = URLConnection.guessContentTypeFromStream(is);
        return mediaType.toString();
    }

}
