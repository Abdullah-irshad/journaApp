//package com.ai.journalApp.service;
//
//import com.ai.journalApp.entity.User;
//import com.ai.journalApp.repository.UserRepository;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ArgumentsSource;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.junit.jupiter.params.provider.ValueSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class UserServiceTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @ParameterizedTest
//    @ArgumentsSource(UserArgumentsProvider.class)
//    public void testCreateUser(User user){
//        assertFalse(userService.createNewUser(user));
//    }
//
//
//
//    @ValueSource(strings = {
//            "thor",
//            "dr strange",
//            "ironman",
//            "spiderman"
//    })
//    @ParameterizedTest
//    public void testFindByUserName(String username){
////        assertNotNull(userRepository.findByUsername("thor"));
////        User user = userRepository.findByUsername("thor");
//        assertNotNull(userRepository.findByUsername(username),"failed for "+username);
//    }
//
//
//    @ParameterizedTest
//    @CsvSource({
//            "1,1,2",
//            "2,2,4",
//            "3,6,9"
//    })
//    public void test(int a,int b,int expected){
//        assertEquals(expected,a+b);
//    }
//
//}
