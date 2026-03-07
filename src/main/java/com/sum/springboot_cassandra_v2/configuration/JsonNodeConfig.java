// package com.sum.springboot_cassandra_v2.configuration;

// import java.util.List;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.cassandra.core.convert.CassandraCustomConversions;

// import com.sum.springboot_cassandra_v2.service.JsonNodeReadConverter;
// import com.sum.springboot_cassandra_v2.service.JsonNodeWriteConverter;

// @Configuration
// public class JsonNodeConfig {

//     @Bean
//     public CassandraCustomConversions customConversions() {
//         return new CassandraCustomConversions(
//             List.of(
//                 new JsonNodeReadConverter(),
//                 new JsonNodeWriteConverter()
//             )
//         );
//     }

// }
