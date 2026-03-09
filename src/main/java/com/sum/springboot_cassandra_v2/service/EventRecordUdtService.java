package com.sum.springboot_cassandra_v2.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sum.springboot_cassandra_v2.model.udt.EventRecord;
import com.sum.springboot_cassandra_v2.repository.EventRecordUdtRepo;

@Service
public class EventRecordUdtService {

    @Autowired
    private EventRecordUdtRepo repo;

    public EventRecord saveEntity(EventRecord eventRecord) {
        return repo.save(eventRecord);
    }

    public Optional<EventRecord> findEntity(String id) {
        return repo.findById(id);
    }
}
