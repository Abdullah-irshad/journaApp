package com.ai.journalApp.controller;


import com.ai.journalApp.entity.User;
import com.ai.journalApp.entity.UserDTO;
import com.ai.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping("/all-users")
    public List<User> getAllUsers(){
        return userService.findAll();
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody UserDTO user){
        User dbuser = userService.findByUsername(user.getUsername());
        if (dbuser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.creatAdmin(dbuser);
        return new ResponseEntity<>(dbuser.getUsername()+" is now admin",HttpStatus.OK);
    }

    @PostMapping("/remove-admin")
    public ResponseEntity<?> removeAdmin(@RequestBody UserDTO user){
        User dbuser = userService.findByUsername(user.getUsername());
        if (dbuser == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.removeAdmin(dbuser);
        return new ResponseEntity<>(dbuser.getUsername()+" removed from admin",HttpStatus.OK);

    }
}
