# Unique ID Generator 
A high-performance, distributed unique ID generator inspired by Twitter's Snowflake system.

## Features
- 64-bit unique ID generation
- Time-sortable IDs
- High concurrency support
- Collision-safe across multiple nodes
- Fully covered with unit tests (JUnit 5)

## ID Structure | Each 64-bit ID is composed of,
- 41 bits : Timestamp (in milliseconds since custom epoch)
- 10 bits : Machine/Node ID
- 12 bits : Sequence number within the same millisecond.
- 1 bit : Unused (sign bit)

## Technologies used 
- Java 23
- Maven
- JUnit 5
- IntelliJ Idea
- Git / GitHub
