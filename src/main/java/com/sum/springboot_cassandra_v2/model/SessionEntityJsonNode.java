package com.sum.springboot_cassandra_v2.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
@Table("session_entity_json_node")
public class SessionEntityJsonNode {

    @PrimaryKey
    @Column("session_id")
    private String sessionId;

    @Column("session_data")
    private String sessionData;
}
