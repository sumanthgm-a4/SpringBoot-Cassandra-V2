# Spring Boot + Cassandra (Docker) – Simple Setup Guide

# 1. Running Cassandra with Docker Compose

Cassandra is started using a Docker Compose service. The container exposes **port 9042**, which is Cassandra's native CQL port used by clients like Spring Boot.

Once the compose file is present, start Cassandra with:

```bash
docker compose -f compose.yaml up --build
```

This will:
- Pull the Cassandra image if it is not present.
- Start the Cassandra container.
- Expose the database on **localhost:9042**.

You can verify that the container is running:

```bash
docker ps
```

---

# 2. Connecting Spring Boot to the Cassandra Container

Spring Boot connects to the containerized Cassandra instance using the configuration in `application.yml`.

Important properties:

- **contact-points**: Host where Cassandra is reachable (`localhost` because the port is exposed from Docker)
- **port**: Cassandra CQL port (`9042`)
- **keyspace-name**: The keyspace used by the application
- **local-datacenter**: Must match the Cassandra node datacenter
- **schema-action**: Automatically creates tables if they don't exist

When the Spring Boot application starts:

1. It connects to **localhost:9042**
2. It connects to the keyspace `test_keyspace`
3. If the keyspace/tables do not exist, Spring Data Cassandra creates them (because `CREATE_IF_NOT_EXISTS` is configured).

This allows the application to immediately start reading and writing data.

---

# 3. Connecting to Cassandra from the Terminal (Linux / macOS)

To access Cassandra directly from the terminal, use the **CQL shell (cqlsh)** inside the running container.

First find the container name:

```bash
docker ps
```

Then connect to Cassandra:

```bash
docker exec -it <container-name> cqlsh
```

Example:

```bash
docker exec -it cassandra cqlsh
```

Once inside the shell, you can run CQL commands:

```sql
DESCRIBE KEYSPACES;

USE test_keyspace;

DESCRIBE TABLES;

SELECT * FROM session_entity_string;
```

This allows you to inspect the database and verify that Spring Boot is writing data correctly.

---

# 4. Application Data Model

The Spring Boot application stores session data in Cassandra using a table mapped to a Java entity.

The table contains:

- **session_id** → Primary key used to identify the record
- **session_data** → A JSON string containing serialized event data

Instead of storing multiple relational columns, the application stores a **serialized JSON representation** of an event record in the `session_data` column.

---

# 5. EventRecord Structure

The serialized JSON represents a complex Java object that contains:
```java
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
```

Because the object contains nested structures (lists, maps, and other objects), it is serialized into JSON before being stored in Cassandra - stored in the **sessionData** field of the **SessionEntityString** POJO.

---

# 6. Serialization and Deserialization Flow

Below are three possible approaches for storing complex nested objects like `EventRecord` in Cassandra.

---

## Scenario 1: Plain String (Manual JSON Serialization)

### Saving Data

1. The application receives an `EventRecord` object.
2. Jackson converts the `EventRecord` POJO into a **JSON string** using `ObjectMapper`.
3. The resulting JSON string is stored directly in a Cassandra `text` column (for example: `session_data`).

Example concept:

EventRecord POJO  
↓  
Jackson ObjectMapper  
↓  
JSON String  
↓  
Stored in Cassandra TEXT column

### Retrieving Data

1. The JSON string is fetched from Cassandra.
2. Jackson `ObjectMapper.readValue()` converts the string back into an `EventRecord` POJO.

This approach keeps Cassandra schema very simple but pushes all structure handling to the application.

---

## Scenario 2: Using `JsonNode` in the Backend

In this approach the backend uses Jackson's `JsonNode` tree model, but Cassandra still stores a **plain string**.

### Saving Data

1. The application receives an `EventRecord` object or JSON payload.
2. Jackson converts it into a `JsonNode`.
3. The `JsonNode` is serialized into a JSON string.
4. That JSON string is stored in a Cassandra `text` column.

Flow:

EventRecord / Incoming JSON  
↓  
Jackson converts to JsonNode  
↓  
JsonNode serialized to JSON string  
↓  
Stored in Cassandra TEXT column

### Retrieving Data

1. The JSON string is retrieved from Cassandra.
2. Jackson parses it into a `JsonNode`.
3. The `JsonNode` can either be:
   - used directly for dynamic processing, or
   - converted into an `EventRecord` POJO.

This approach is useful when the structure may change or when partial JSON manipulation is needed before converting to POJOs.

Important point: **Cassandra still stores only a string.**

---

## Scenario 3: Using Cassandra UDTs (Structured Storage)

In this approach the nested objects are stored as **native Cassandra User Defined Types (UDTs)** instead of JSON strings.

The Cassandra schema explicitly understands the structure of the data.

### Example UDTs

Based on the following Java classes:

Metadata  
Action  
User  

Sample Cassandra CQL to create the UDTs:
```cql
CREATE TYPE metadata_udt (
key text,
value text
);

CREATE TYPE action_udt (
action_type text,
description text
);

CREATE TYPE user_udt (
user_id text,
name text,
role text
);
```

### Table Definition
```cql
CREATE TABLE event_record (
event_id text PRIMARY KEY,
actor frozen<user_udt>,
actions list<frozen<action_udt>>,
metadata map<text, frozen<metadata_udt>>,
created_at timestamp
);
```

### Saving Data

1. The application receives an `EventRecord` object.
2. Spring Data Cassandra maps:
   - `User` → `user_udt`
   - `Action` → `action_udt`
   - `Metadata` → `metadata_udt`
3. Cassandra stores the structured data directly in UDT columns.

Flow:

EventRecord POJO  
↓  
Spring Data Cassandra Mapping  
↓  
Mapped to UDT columns  
↓  
Stored as structured Cassandra types

### Retrieving Data

1. Cassandra returns UDT values.
2. Spring Data Cassandra automatically converts them back into:
   - `User`
   - `List<Action>`
   - `Map<String, Metadata>`
3. The application receives a fully reconstructed `EventRecord` POJO.

This approach provides strong schema structure in Cassandra and avoids JSON serialization entirely.

---

# 7. Handling Java Time Serialization

The `EventRecord` object contains a `LocalDateTime` field.  
By default, Jackson does not handle Java 8 date/time types.

To properly serialize and deserialize `LocalDateTime`, the application registers the **Jackson Java Time module** with the `ObjectMapper`.

This enables correct JSON conversion of timestamp fields during both save and read operations.

---

# 8. Summary

The system works as follows:

1. **Docker Compose** runs a Cassandra database container.
2. **Spring Boot** connects to Cassandra using `localhost:9042`.
3. **Spring Data Cassandra** manages schema creation automatically.
4. Complex Java objects (`EventRecord`) are **serialized to JSON strings**.
5. The JSON is stored in a Cassandra table column.
6. When reading data, the JSON is **deserialized back into Java objects** using Jackson.

This approach keeps the Cassandra schema simple while still supporting complex application data structures.