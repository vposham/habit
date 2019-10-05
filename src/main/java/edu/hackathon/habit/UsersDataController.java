package edu.hackathon.habit;

import edu.hackathon.habit.db.User;
import edu.hackathon.habit.db.UserDbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/user")
public class UsersDataController {

    @Autowired
    UserDbUtil usersRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        usersRepository.save(user);
        return user;
    }

    @GetMapping
    public List listUsers() {
        return usersRepository.findAll();
    }

    @PutMapping("/{userId}")
    public User updateUser(@RequestBody User user, @PathVariable String userId) {
        user.setUserId(userId);
        usersRepository.save(user);
        return user;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        usersRepository.deleteById(userId);
        return userId;
    }
}
