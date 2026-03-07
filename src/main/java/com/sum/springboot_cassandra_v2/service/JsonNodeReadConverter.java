// package com.sum.springboot_cassandra_v2.service;

// import org.springframework.data.convert.ReadingConverter;

// import com.fasterxml.jackson.databind.JavaType;
// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.type.TypeFactory;
// import com.fasterxml.jackson.databind.util.Converter;

// @ReadingConverter
// public class JsonNodeReadConverter implements Converter<String, JsonNode> {

//     private final ObjectMapper mapper = new ObjectMapper();

//     @Override
//     public JsonNode convert(String source) {
//         try {
//             return mapper.readTree(source);
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }

//     @Override
//     public JavaType getInputType(TypeFactory typeFactory) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'getInputType'");
//     }

//     @Override
//     public JavaType getOutputType(TypeFactory typeFactory) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'getOutputType'");
//     }
// }