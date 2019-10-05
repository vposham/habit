package edu.hackathon.habit;

import edu.hackathon.habit.model.LoginRequest;
import edu.hackathon.habit.model.LoginResponse;
import edu.hackathon.habit.model.RecordingRespMeta;
import edu.hackathon.habit.services.GeoRecorderDataService;
import edu.hackathon.habit.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    GeoRecorderDataService geoRecorderDataService;

    @Autowired
    LoginService loginService;

    @PostMapping(value = "/validate")
    @ResponseBody
    public LoginResponse validate(@RequestBody LoginRequest request, HttpServletResponse response) {

        String userId = loginService.getUserIdUponLogin(request);
        if(userId == null){
            LoginResponse out = null;
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }else{
            LoginResponse out = new LoginResponse();
            out.setUserId(userId);
            return out;
        }

    }

    @GetMapping(value = "{userId}/geo")
    @ResponseBody
    public List<RecordingRespMeta> getRecordingsByLocation(@PathVariable String userId, @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude) {
        return geoRecorderDataService.getRecordingByCoordinate(userId, latitude, longitude);
    }


    @GetMapping(value = "{userId}/{recId}/download")
    @ResponseBody
    public byte[] downloadRecording(@PathVariable String userId, @PathVariable String recId) {
        return new byte[0];
    }

    @PostMapping(value = "{userId}/save")
    @ResponseBody
    public RecordingRespMeta saveRecording(@PathVariable String userId, @RequestParam("file") MultipartFile file, @RequestParam("image") MultipartFile image, @RequestParam("latitude") String latitude, @RequestParam("longitude") String longitude) {
        RecordingRespMeta out = new RecordingRespMeta();
        out.setIsPrivate(false);
        out.setLatitude("");
        out.setLongitude("");
        out.setRecId("");
        out.setTags(new ArrayList<>());
        return out;
    }


}