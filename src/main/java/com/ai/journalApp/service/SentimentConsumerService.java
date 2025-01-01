package com.ai.journalApp.service;

import com.ai.journalApp.model.SentimentalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "weekly-sentiments",groupId = "weekly-sentiment-group")
    public void consume(SentimentalData sentimentalData){
        sendEmail(sentimentalData);
    }
    private void sendEmail(SentimentalData sentimentalData){
        emailService.sendEmail(sentimentalData.getEmail(),"Sentiment for previous week", sentimentalData.getSentimental());
    }
}
