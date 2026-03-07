# Spring Boot + Cassandra (Docker) – Simple Setup Guide

## 1. Running Cassandra with Docker Compose

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

### Saving Data

1. The application receives an `EventRecord` object.
2. The object is converted into a **JSON tree** using Jackson.
3. The JSON is converted to a **formatted string**.
4. That string is stored in the `session_data` column.

### Retrieving Data

1. The JSON string is retrieved from Cassandra.
2. It is parsed into a `JsonNode`.
3. Jackson converts the `JsonNode` back into an `EventRecord` object.

This allows the application to store complex nested objects while keeping the Cassandra schema simple.

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