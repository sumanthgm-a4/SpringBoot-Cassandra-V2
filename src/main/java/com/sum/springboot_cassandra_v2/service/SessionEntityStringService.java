package com.sum.springboot_cassandra_v2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sum.springboot_cassandra_v2.model.EventRecord;
import com.sum.springboot_cassandra_v2.model.SessionEntityString;
import com.sum.springboot_cassandra_v2.repository.SessionEntityStringRepo;

@Service
public class SessionEntityStringService {

    @Autowired
    private SessionEntityStringRepo repo;

    @Autowired
    private ObjectMapper objectMapper;

    public SessionEntityString saveEntity(EventRecord record) {
        SessionEntityString sessionEntityString = new SessionEntityString();

        String sessionData = null;
        try {
            sessionData = objectMapper.writeValueAsString(record);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sessionEntityString.setSessionId(record.getEventId());
        sessionEntityString.setSessionData(sessionData);

        System.out.println(sessionEntityString);

        repo.save(sessionEntityString);

        return sessionEntityString;
    }

    public Optional<EventRecord> findEntity(String id) {
        Optional<SessionEntityString> sessionEntityOptional = repo.findById(id);

        if (sessionEntityOptional.isPresent()) {
            EventRecord eventRecord = null;
            try {
                eventRecord = objectMapper.readValue(sessionEntityOptional.get().getSessionData(), EventRecord.class);
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Optional.of(eventRecord);
        }

        return Optional.empty();
    }

}   
