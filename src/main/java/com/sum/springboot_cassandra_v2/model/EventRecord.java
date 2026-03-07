package com.sum.springboot_cassandra_v2.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Data
public class EventRecord {
    
    private String eventId;

    // Nested POJO
    private User actor;

    // List of POJOs
    private List<Action> actions;

    // Map<String, POJO>
    private Map<String, Metadata> metadata;

    private LocalDateTime createdAt;
}
