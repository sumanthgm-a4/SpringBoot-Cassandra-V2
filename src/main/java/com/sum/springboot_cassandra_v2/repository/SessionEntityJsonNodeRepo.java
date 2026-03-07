package com.sum.springboot_cassandra_v2.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.sum.springboot_cassandra_v2.model.SessionEntityJsonNode;

@Repository
public interface SessionEntityJsonNodeRepo extends CassandraRepository<SessionEntityJsonNode, String> {

}
