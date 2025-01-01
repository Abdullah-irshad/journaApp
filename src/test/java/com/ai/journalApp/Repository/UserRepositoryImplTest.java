package com.ai.journalApp.Repository;


import com.ai.journalApp.entity.User;
import com.ai.journalApp.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    public void getUser(){
        List<User> list = userRepository.getUsersForSA();
        System.out.println(list);
    }
}
