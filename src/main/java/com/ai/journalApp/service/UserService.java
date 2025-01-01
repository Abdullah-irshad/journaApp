package com.ai.journalApp.service;
import com.ai.journalApp.entity.User;
import com.ai.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public void createNewUser(User user){
        if (user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new Error("username & password should not be empty");
        }
      try {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          user.setDateTime(LocalDateTime.now());
          user.setRoles(List.of("USER"));
          userRepository.save(user);
      }catch (Exception error){
          logger.error("Error occurred for {} {}:",user.getUsername(), error.getMessage());
          throw error;
      }
    }


    public void saveUser(User user){
        userRepository.save(user);
    }


    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public String verify(User user){
        Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                user.getPassword()));

        if (authentication.isAuthenticated()){
            return jwtService.generateToken(authentication.getName());
        }
        throw new Error("incorrect credentials");
    }

    public void creatAdmin(User user) {
        user.setRoles(List.of("ADMIN"));
        saveUser(user);
    }

    public void removeAdmin(User user) {
        user.setRoles(List.of("USER"));
        saveUser(user);
    }
}

