package com.ai.journalApp.service;

import com.ai.journalApp.entity.JournalEntry;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;



@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private RedisService redisService;

    @Disabled
    @Test
    public void redisTest(){
//        redisTemplate.opsForValue().set("email","sample@email.com");
        Object email = redisTemplate.opsForValue().get("cnt");
        System.out.println(email);
    }


    @Test
    @Disabled
    public void saveData(){
        List<JournalEntry> list = journalEntryService.getAllEntriesOfUser("ironman");
        redisService.set("journal_ironman",list,10l);
    }


    @Test
//    @Disabled
    public void getData(){
        List<JournalEntry> list = redisService.get("journal_ironman",new TypeReference<List<JournalEntry>>(){});
        System.out.println(list);
    }


}
