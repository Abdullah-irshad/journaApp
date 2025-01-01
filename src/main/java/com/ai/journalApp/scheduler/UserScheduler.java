package com.ai.journalApp.scheduler;

import com.ai.journalApp.entity.JournalEntry;
import com.ai.journalApp.entity.User;
import com.ai.journalApp.enums.Sentiments;
import com.ai.journalApp.model.SentimentalData;
import com.ai.journalApp.repository.UserRepositoryImpl;
import com.ai.journalApp.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class UserScheduler {
    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private KafkaTemplate<String,SentimentalData> kafkaTemplate;

    @Autowired
    private EmailService emailService;


    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUsersAndSendSaMail(){
        List<User> users = userRepository.getUsersForSA();
        for (User user: users){
            Map<Sentiments,Integer> sentimentCount = new HashMap<>();
            LocalDateTime sevenDayAgo = LocalDateTime.now().minus(7,ChronoUnit.DAYS);
            int cnt =0;
            Sentiments mostFrequent = null;
            for (JournalEntry journalEntry : user.getJournalEntries()){
               if (journalEntry.getDate().isAfter(sevenDayAgo)){
                   Sentiments sentiment = journalEntry.getSentiments();
                   int count = sentimentCount.getOrDefault(sentiment,0)+1;
                   sentimentCount.put(sentiment,count);
                   if (count > cnt){
                       cnt = count;
                       mostFrequent = sentiment;
                   }
               }
            }

            if (mostFrequent != null){
                SentimentalData sentimentalData = SentimentalData.builder().email(user.getEmail())
                                .sentimental("Sentiment for last 7 days "+mostFrequent).build();
                try{
                    kafkaTemplate.send("weekly-sentiments",sentimentalData.getEmail(),sentimentalData);
                }catch (Exception e){
                    emailService.sendEmail(sentimentalData.getEmail(),"Sentiment for previous week", sentimentalData.getSentimental());
                }
            }
        }
    }


//    @Scheduled(cron = "0 0 9 * * SUN")
////@Scheduled(cron = "*/1 * * * * ?")
//public void fetchUsersAndSendSaMail(){
//        List<User> users = userRepository.getUsersForSA();
//        for (User user : users){
//            List<JournalEntry> journalEntries = user.getJournalEntries();
//            List<Sentiments> filtered = journalEntries.stream()
//                    .filter(x->x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS)))
//                    .map(JournalEntry::getSentiments)
//                    .toList();
//
//            HashMap<Sentiments,Integer> sentCount = new HashMap<>();
//            for (Sentiments sentiment:filtered){
//                sentCount.put(sentiment,sentCount.getOrDefault(sentiment,0)+1);
//            }
//
//            Sentiments mostFrequent = null;
//            int cnt  =0;
//            for (Map.Entry<Sentiments, Integer> entry : sentCount.entrySet()){
//                if (entry.getValue() > cnt){
//                    cnt = entry.getValue();
//                    mostFrequent = entry.getKey();
//                }
//            }
//
//
//            if (mostFrequent != null){
//                emailService.sendEmail(user.getEmail(),"Sentiment for last 7 days",mostFrequent.toString());
//            }
//        }
//    }
}
