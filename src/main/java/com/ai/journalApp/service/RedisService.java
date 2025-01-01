package com.ai.journalApp.service;

import com.ai.journalApp.serializer.ObjectIdDeserializer;
import com.ai.journalApp.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public RedisService(RedisTemplate<String,String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);




        // Register custom serializer and deserializer for ObjectId

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectId.class,new ObjectIdSerializer());
        module.addDeserializer(ObjectId.class,new ObjectIdDeserializer());
        this.mapper.registerModule(module);
    }

    public <T> T get(String key, TypeReference<T> typeReference){
      try{
          String json = redisTemplate.opsForValue().get(key);
          if (json == null) {
              log.warn("Key '{}' not found in Redis", key);
              return null;
          }
          return mapper.readValue(json,typeReference);
      }catch (Exception e){
          log.error("Failed to get key '{}' from Redis", key, e);
          return null;
      }
    }

    public void set(String key,Object value,Long ttl){
        try{
            String jsonValue = mapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.MINUTES);
        }catch (Exception e){
            log.error("Failed to set key '{}' with value '{}' in Redis", key, value, e);
        }
    }

    public void delete(String key){
        try{
            redisTemplate.delete(key);
        }catch (Exception e){
            log.error("Exception ",e);
        }
    }

}
