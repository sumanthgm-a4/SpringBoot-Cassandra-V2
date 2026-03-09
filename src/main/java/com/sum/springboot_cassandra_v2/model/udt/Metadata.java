package com.sum.springboot_cassandra_v2.model.udt;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import lombok.Data;

@Data
@UserDefinedType("metadata_udt")
public class Metadata {
    private String key;
    private String value;
}
