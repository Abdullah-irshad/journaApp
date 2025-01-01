package com.ai.journalApp.repository;

import com.ai.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import java.util.List;


@Component
public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUsersForSA(){
        Query query = new Query();

//        query.addCriteria(Criteria.where("username").is("thor"));

//        query.addCriteria(Criteria.where("email").exists(true));
//        query.addCriteria(Criteria.where("sentimentalAnalysis").is(true));


//        query.addCriteria(Criteria.where("email").regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));


        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(
                Criteria.where("email").exists(true).ne(null).ne(""),
                Criteria.where("sentimentalAnalysis").is(true)
        ));

        return mongoTemplate.find(query,User.class);
    }

}
