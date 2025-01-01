package com.ai.journalApp.controller;

import com.ai.journalApp.entity.JournalEntry;
import com.ai.journalApp.entity.User;
import com.ai.journalApp.service.JournalEntryService;
import com.ai.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            List<JournalEntry> all = journalEntryService.getAllEntriesOfUser(username);
            if (all != null && !all.isEmpty()){
                return new ResponseEntity<>(all,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry entry){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.saveEntry(entry,username);
            return new ResponseEntity<>(entry,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getGeneralById(@PathVariable ObjectId id){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
        return journalEntry.map(
                entry -> new ResponseEntity<>(entry,HttpStatus.OK)
        ).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<?> deleteJournalEntry(@PathVariable ObjectId id){
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (isOwner(id, user)){
            return new ResponseEntity<>("You are not authorized to delete this journal entry", HttpStatus.FORBIDDEN);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        journalEntryService.deleteById(id,username);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id,@RequestBody JournalEntry entry){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        if (isOwner(id, user)){
            return new ResponseEntity<>("You are not authorized to update this journal entry", HttpStatus.FORBIDDEN);
        }

        if (entry == null) {
            return new ResponseEntity<>("Invalid entry data", HttpStatus.BAD_REQUEST);
        }


        JournalEntry oldEntry = journalEntryService.findById(id).orElse(null);
        if(oldEntry != null){
            if (entry.getTitle() != null){
                oldEntry.setTitle(entry.getTitle());
            }
            if (entry.getContent() != null){
                oldEntry.setContent(entry.getContent());
            }
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private boolean isOwner(Object id, User user){
        return user.getJournalEntries().stream()
                .noneMatch(e -> e.getId().equals(id));
    }
}
