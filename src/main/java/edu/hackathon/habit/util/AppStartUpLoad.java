package edu.hackathon.habit.util;

import edu.hackathon.habit.db.Recording;
import edu.hackathon.habit.db.RecordingDbUtil;
import edu.hackathon.habit.db.User;
import edu.hackathon.habit.db.UserDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.Security;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@DependsOn(value= {"userDbUtil"})
public class AppStartUpLoad {

    @Autowired
    UserDbUtil userDbUtil;

    @Autowired
    RecordingDbUtil recordingDbUtil;


    @PostConstruct
    public void init(){


        try{

            long count = userDbUtil.count();
            if(count == 12){
                insertRecords();
                return;
            }

            User p1 = new User("dilip1","Tester123",false);
            User p2 = new User("sachin1","Tester123",false);
            User p3 = new User("praveen1","Tester123",false);
            User p4 = new User("mihai1","Tester123",false);
            User p5 = new User("posham1","Tester123",false);
            User p6 = new User("norman1","Tester123",false);

            User pr1 = new User("dilip2","Tester123",true);
            User pr2 = new User("sachin2","Tester123",true);
            User pr3 = new User("praveen2","Tester123",true);
            User pr4 = new User("mihai2","Tester123",true);
            User pr5 = new User("posham2","Tester123",true);
            User pr6 = new User("norman2","Tester123",true);

            List<User> userList = new ArrayList<>();
            userList.add(p1);
            userList.add(p2);
            userList.add(p3);
            userList.add(p4);
            userList.add(p5);
            userList.add(p6);
            userList.add(pr1);
            userList.add(pr2);
            userList.add(pr3);
            userList.add(pr4);
            userList.add(pr5);
            userList.add(pr6);



            userDbUtil.saveAll(userList);

            insertRecords();

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void insertRecords(){


        InputStream inEnc = null;
        InputStream inEnc1 = null;
        try{
            User u = userDbUtil.findByUsername("dilip1");

            String userId = u.getUserId();

            Date date = new Date();
            Timestamp ts=new Timestamp(date.getTime());
            System.out.println(ts);

            String transcript = "This is a Test I repeat this is a test";

            List<String> tags = new ArrayList<>();
            tags.add("Test");
            tags.add("warning");
            tags.add("Hurricane");
            tags.add("Delray Beach");
            tags.add("Boca Raton");
            tags.add("Boynton Beach");

            inEnc = getClass().getResourceAsStream("/recordings/recording1.wav");
            inEnc1 = getClass().getResourceAsStream("/recordings/hurricane.jpg");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inEnc1.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
            byte[] picbyteArray = buffer.toByteArray();

            ByteArrayOutputStream buffer1 = new ByteArrayOutputStream();
            int nRead1;
            byte[] data1 = new byte[1024];
            while ((nRead1 = inEnc.read(data1, 0, data1.length)) != -1) {
                buffer1.write(data1, 0, nRead1);
            }

            buffer1.flush();
            byte[] vidbyteArray = buffer1.toByteArray();

            Recording record1 =new Recording( userId,  vidbyteArray, transcript, tags,  false,  "26.401097",  "-80.120519",  "BOCA RATON", ts.toString(), false, "", null);


            Recording record2 =new Recording( userId,  vidbyteArray, transcript, tags,  false,  "26.401097",  "-80.120519",  "DELRAY BEACH", ts.toString(), false, "", null);

            Recording record3 =new Recording( userId,  vidbyteArray, transcript, tags,  false,  "26.401097",  "-80.120519",  "BOYNTON BEACH", ts.toString(), false, "", null);


            Recording record4 =new Recording( userId,  vidbyteArray, transcript, tags,  true,  "26.401097",  "-80.120519",  "BOCA RATON", ts.toString(), false, "", null);


            Recording record5 =new Recording( userId,  vidbyteArray, transcript, tags,  true,  "26.401097",  "-80.120519",  "DELRAY BEACH", ts.toString(), false, "", null);


            Recording record6 =new Recording( userId,  vidbyteArray, transcript, tags,  true,  "26.401097",  "-80.120519",  "BOYNTON BEACH", ts.toString(), false, "", null);


            Recording record7 =new Recording( userId,  null, transcript, tags,  false,  "26.401097",  "-80.120519",  "BOCA RATON", ts.toString(), true, "Sample", picbyteArray);

            Recording record8 =new Recording( userId,  null, transcript, tags,  false,  "26.401097",  "-80.120519",  "DELRAY BEACH", ts.toString(), true, "Sample", picbyteArray);

            Recording record9 =new Recording( userId,  null, transcript, tags,  false,  "26.401097",  "-80.120519",  "BOYNTON BEACH", ts.toString(), true, "Sample", picbyteArray);

            Recording record10 =new Recording( userId,  null, transcript, tags,  true,  "26.401097",  "-80.120519",  "BOCA RATON", ts.toString(), true, "Sample", picbyteArray);

            Recording record11 =new Recording( userId,  null, transcript, tags,  true,  "26.401097",  "-80.120519",  "DELRAY BEACH", ts.toString(), true, "Sample", picbyteArray);
            Recording record12 =new Recording( userId,  null, transcript, tags,  true,  "26.401097",  "-80.120519",  "BOYNTON BEACH", ts.toString(), true, "Sample", picbyteArray);

            List<Recording> recordingList = new ArrayList<>();
            recordingList.add(record1);
            recordingList.add(record2);
            recordingList.add(record3);
            recordingList.add(record4);
            recordingList.add(record5);
            recordingList.add(record6);
            recordingList.add(record7);
            recordingList.add(record8);
            recordingList.add(record9);
            recordingList.add(record10);
            recordingList.add(record11);
            recordingList.add(record12);

            recordingDbUtil.saveAll(recordingList);



        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (inEnc != null) {
                    inEnc.close();
                }
            } catch (IOException var2) {
            }
            try {
                if (inEnc1 != null) {
                    inEnc1.close();
                }
            } catch (IOException var2) {
            }
        }
    }


}
