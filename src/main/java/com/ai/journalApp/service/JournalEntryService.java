package com.ai.journalApp.service;

import com.ai.journalApp.entity.JournalEntry;
import com.ai.journalApp.entity.User;
import com.ai.journalApp.repository.JournalEntryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;


    public List<JournalEntry> getAllEntriesOfUser(String username){
        String key = username+"_journal";
        List<JournalEntry> list = redisService.get(key, new TypeReference<List<JournalEntry>>(){});
        if (list != null){
            return list;
        }
        User user = userService.findByUsername(username);
        if (user == null){
            throw new Error("Unauthorized request");
        }
        redisService.set(key,user.getJournalEntries(),10L);
        return user.getJournalEntries();
    }


    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username) {
       try {
           User user = userService.findByUsername(username);
           journalEntry.setDate(LocalDateTime.now());
           JournalEntry savedEntry = journalEntryRepository.save(journalEntry);
           user.getJournalEntries().add(savedEntry);
           userService.saveUser(user);
       }catch (Exception e){
           System.out.println(e);
           throw  new RuntimeException("An Error occurred while saving the journal entry");
       }
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String username){
        User user = userService.findByUsername(username);
        user.getJournalEntries().removeIf(entry -> entry.getId().equals(id));
        journalEntryRepository.deleteById(id);
        userService.saveUser(user);
    }

}
