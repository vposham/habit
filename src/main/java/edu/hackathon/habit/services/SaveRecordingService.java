package edu.hackathon.habit.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Topic;
import edu.hackathon.habit.db.Recording;
import edu.hackathon.habit.db.RecordingDbUtil;
import edu.hackathon.habit.db.UserDbUtil;
import edu.hackathon.habit.model.KeywordsApiResponse;
import edu.hackathon.habit.model.RecordingRespMeta;
import edu.hackathon.habit.util.FinderUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaveRecordingService {

    @Autowired
    FinderUtils finderUtils;

    @Autowired
    UserDbUtil userRepo;

    @Autowired
    RecordingDbUtil recordingRepo;

    @Autowired
    ObjectMapper mapper;

    public RecordingRespMeta populateInfo(String userId, MultipartFile recording, MultipartFile image, String latitude, String longitude, String transcript, boolean isComplaint, String title) throws IOException {
        String city = finderUtils.findCity(latitude, longitude);
        LocalDateTime datetime = LocalDateTime.now();
        String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(datetime);
        Recording newRecording = new Recording();
        List<String> tags = new ArrayList<>();
        if (transcript != null) {
            tags = extractTagsFromTranscript(transcript);
            newRecording.setTags(tags);
        }

        boolean isPrivatePost = userRepo.findByUserId(userId).isPrivate;

        newRecording.setRecording(recording.getBytes());
        newRecording.setCity(city);
        newRecording.setComplaint(isComplaint);
        newRecording.setUserId(userId);
        newRecording.setTranscript(transcript);
        newRecording.setPrivate(isPrivatePost);
        newRecording.setLatitude(latitude);
        newRecording.setLongitude(longitude);
        newRecording.setTimeStamp(timestamp);
        newRecording.setImage(image.getBytes());
        newRecording.setTitle(title);

        newRecording = recordingRepo.save(newRecording);

        RecordingRespMeta out = new RecordingRespMeta();

        out.setIsPrivate(isPrivatePost);
        out.setLatitude(latitude);
        out.setLongitude(longitude);
        out.setRecId(newRecording.getRecordingId());
        out.setTags(tags);
        return out;

    }

    public List<String> extractTagsFromTranscript(String transcript) {
        OkHttpClient client = new OkHttpClient();
        try {
            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"text\"\r\n\r\n" + transcript + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"api_type\"\r\n\r\nkeywords\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            Request request = new Request.Builder()
                    .url("https://www.paralleldots.com/api/demos")
                    .post(body)
                    .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .addHeader("accept-encoding", "application/json")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("Accept", "*/*")
                    .addHeader("Host", "www.paralleldots.com")
                    .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------739096014594056183623904")
                    .addHeader("Cookie", "__cfduid=d34abd229dc1d779559bb32888b53a1f01570299208")
                    .addHeader("Content-Length", "324")
                    .addHeader("Connection", "keep-alive")
                    .build();

            Response response = client.newCall(request).execute();

            KeywordsApiResponse apiResp = mapper.readValue(response.body().string(), KeywordsApiResponse.class);
            if (apiResp.getCode() != 200) {
                throw new IllegalStateException("API is down");
            }
            List<String> out = new ArrayList<>();
            apiResp.getKeywords().stream().filter(x -> x.getConfidenceScore() > 0.5).collect(Collectors.toList()).forEach(y -> out.add(y.getKeyword()));
            return out;
        } catch (Exception e) {
            return backupTagAnalyzer(transcript);
        }

    }

    private List<String> backupTagAnalyzer(String transcript) {
        List<String> tags = new ArrayList<>();
        TextRazor client = new TextRazor("your api key");
        client.addExtractor("topics");

        AnalyzedText response = null;
        try {
            response = client.analyze(transcript);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Topic topic : response.getResponse().getTopics()) {
            tags.add(topic.getLabel());
        }
        return tags;
    }

}
