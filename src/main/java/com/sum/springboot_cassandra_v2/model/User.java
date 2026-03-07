package com.sum.springboot_cassandra_v2.model;

import lombok.Data;

@Data
public class User {
    private String userId;
    private String name;
    private String role;
}
