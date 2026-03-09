package com.sum.springboot_cassandra_v2.model.udt;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data
@UserDefinedType("user_udt")
public class User {
    @Column("user_id")
    private String userId;
    private String name;
    private String role;
}
