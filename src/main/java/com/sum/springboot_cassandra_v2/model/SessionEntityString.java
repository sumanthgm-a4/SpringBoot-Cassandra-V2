package com.sum.springboot_cassandra_v2.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Data
@Table("session_entity_string")
public class SessionEntityString {

    @PrimaryKey
    @Column("session_id")
    private String sessionId;

    @Column("session_data")
    private String sessionData;
}
