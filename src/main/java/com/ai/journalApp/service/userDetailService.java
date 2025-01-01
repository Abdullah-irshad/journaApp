package com.ai.journalApp.service;

import com.ai.journalApp.entity.User;
import com.ai.journalApp.entity.UserPrincipal;
import com.ai.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

//@Component
//public class userDetailService implements UserDetailsService {
//    @Autowired
//    private UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        com.ai.journalApp.entity.User user = userRepository.findByUsername(username);
//        if (user == null){
//            throw new UsernameNotFoundException("User not found with username: "+username);
//        }
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRoles().toArray(new String[0]))
//                .build();
//    }
//}

@Component
public class userDetailService implements UserDetailsService {
   @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("user not found");
        }
        return new UserPrincipal(user);
    }
}