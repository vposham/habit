package edu.hackathon.habit.services;

import edu.hackathon.habit.db.User;
import edu.hackathon.habit.db.UserDbUtil;
import edu.hackathon.habit.model.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserDbUtil userDbUtil;

    public String getUserIdUponLogin(LoginRequest request){

        try{

            String userName = request.getUsername();
            String pwd = request.getPassword();
            if(userName==null || pwd ==null ||  userName.trim().isEmpty() || pwd.trim().isEmpty()){
                return null;
            }
            User user = userDbUtil.findByUsername(request.getUsername().trim().toLowerCase());

            if(user!=null && user.password.equals(pwd.trim())){
                //good
                return user.getUserId();
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
