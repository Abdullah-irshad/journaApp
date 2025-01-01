package com.ai.journalApp.controller;

import com.ai.journalApp.cache.AppCache;
import com.ai.journalApp.entity.User;
import com.ai.journalApp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private AppCache appCache;

    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user){
      try{
          userService.createNewUser(user);
          return new ResponseEntity<>("user has been created", HttpStatus.CREATED);
      }catch (Exception error){
          return new ResponseEntity<>("username is not available",HttpStatus.BAD_REQUEST);
      }
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        return userService.verify(user);
    }

    @GetMapping("/cached")
    public String getCached(){
        return appCache.APP_CACHE.get("sample");
    }
    @GetMapping("/clear-cached")
    public void clearCached(){
        appCache.init();
    }
}