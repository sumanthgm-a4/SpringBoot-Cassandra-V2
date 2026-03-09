package com.sum.springboot_cassandra_v2.model.udt;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data
@UserDefinedType("action_udt")
public class Action {
    @Column("action_type")
    private String actionType;
    private String description;
}
