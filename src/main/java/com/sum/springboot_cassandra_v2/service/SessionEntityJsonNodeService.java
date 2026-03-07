package com.sum.springboot_cassandra_v2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sum.springboot_cassandra_v2.model.EventRecord;
import com.sum.springboot_cassandra_v2.model.SessionEntityJsonNode;
import com.sum.springboot_cassandra_v2.repository.SessionEntityJsonNodeRepo;

@Service
public class SessionEntityJsonNodeService {

    @Autowired
    private SessionEntityJsonNodeRepo repo;

    @Autowired
    private ObjectMapper objectMapper;

    public Optional<EventRecord> findEntity(String id) {
        Optional<SessionEntityJsonNode> sessionEntityOptional = repo.findById(id);

        if (sessionEntityOptional.isPresent()) {

            EventRecord eventRecord = null;

            try {
                JsonNode node = objectMapper.readTree(sessionEntityOptional.get().getSessionData());
                eventRecord = objectMapper.treeToValue(node, EventRecord.class);
            } catch (JsonProcessingException | IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return Optional.of(eventRecord);
        }

        return Optional.empty();
    }

    public SessionEntityJsonNode saveEntity(EventRecord record) {
        SessionEntityJsonNode sessionEntityJsonNode = new SessionEntityJsonNode();

        JsonNode sessionData = null;
        try {
            sessionData = objectMapper.valueToTree(record);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sessionEntityJsonNode.setSessionId(record.getEventId());
        sessionEntityJsonNode.setSessionData(sessionData.toPrettyString());

        System.out.println(sessionEntityJsonNode);
        System.out.println(sessionEntityJsonNode.getSessionData().getClass());

        repo.save(sessionEntityJsonNode);

        return sessionEntityJsonNode;
    }

}
