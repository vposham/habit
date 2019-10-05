package edu.hackathon.habit.util;

import edu.hackathon.habit.db.User;
import edu.hackathon.habit.db.UserDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@DependsOn(value= {"userDbUtil"})
public class AppStartUpLoad {

    @Autowired
    UserDbUtil userDbUtil;


    @PostConstruct
    public void init(){


        try{

            User p1 = new User("dilip1","Tester123",false);
            User p2 = new User("sachin1","Tester123",false);
            User p3 = new User("praveen1","Tester123",false);
            User p4 = new User("mihai1","Tester123",false);
            User p5 = new User("posham1","Tester123",false);
            User p6 = new User("norman1","Tester123",false);

            List<User> userList = new ArrayList<>();
            userList.add(p1);
            userList.add(p2);
            userList.add(p3);
            userList.add(p4);
            userList.add(p5);
            userList.add(p6);



            userDbUtil.saveAll(userList);

        }catch (Exception e){
            e.printStackTrace();
        }



//
//
//
//        u1 = new User("dilip2","Tester123",true);
//        u1 = new User("sachin2","Tester123",true);
//        u1 = new User("praveen2","Tester123",true);
//        u1 = new User("mihai2","Tester123",true);
//        u1 = new User("posham2","Tester123",true);
//        u1 = new User("norman2","Tester123",true);


        User userresultList = userDbUtil.findByUsername("dilip1");
        System.out.println(userresultList.username);
    }


}
