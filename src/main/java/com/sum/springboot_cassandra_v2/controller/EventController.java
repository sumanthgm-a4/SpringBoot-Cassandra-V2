package com.sum.springboot_cassandra_v2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sum.springboot_cassandra_v2.model.EventRecord;
import com.sum.springboot_cassandra_v2.model.SessionEntityJsonNode;
import com.sum.springboot_cassandra_v2.model.SessionEntityString;
import com.sum.springboot_cassandra_v2.service.EventRecordUdtService;
import com.sum.springboot_cassandra_v2.service.SessionEntityJsonNodeService;
import com.sum.springboot_cassandra_v2.service.SessionEntityStringService;

@RestController
public class EventController {

    @Autowired
    private SessionEntityStringService stringService;

    @Autowired
    private SessionEntityJsonNodeService jsonNodeService;

    @Autowired
    private EventRecordUdtService eventRecordUdtService;

    @GetMapping("/events-string/{id}")
    private EventRecord getOneEventString(@PathVariable String id) {
        return stringService.findEntity(id).orElse(null);
    }

    @PostMapping("/events-string")
    private SessionEntityString saveEventString(@RequestBody EventRecord record) {
        return stringService.saveEntity(record);
    }

    @GetMapping("/events-jsonnode/{id}")
    private EventRecord getOneEventJsonNode(@PathVariable String id) {
        return jsonNodeService.findEntity(id).orElse(null);
    }

    @PostMapping("/events-jsonnode")
    private SessionEntityJsonNode saveEventJsonNode(@RequestBody EventRecord record) {
        return jsonNodeService.saveEntity(record);
    }

    @GetMapping("/events-udt/{id}")
    private com.sum.springboot_cassandra_v2.model.udt.EventRecord getOneEventUdt(@PathVariable String id) {
        return eventRecordUdtService.findEntity(id).orElse(null);
    }

    @PostMapping("/events-udt")
    private com.sum.springboot_cassandra_v2.model.udt.EventRecord saveEventUdt(@RequestBody com.sum.springboot_cassandra_v2.model.udt.EventRecord record) {
        return eventRecordUdtService.saveEntity(record);
    }
}
