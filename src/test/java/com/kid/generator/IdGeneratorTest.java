package com.kid.generator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.*;
import java.util.HashSet;
import java.util.Set;

public class IdGeneratorTest {

    @Test
    void testIdUniqueness() {
        IdGenerator generator = new IdGenerator(1);
        Set<Long> idSet = new HashSet<>();

        for (int i = 0; i < 1000; i++) {
            long id = generator.generateId();
            assertTrue(idSet.add(id), "ID collision detected!");
        }
    }

    @Test
    void testIdOrdering() {
        IdGenerator generator = new IdGenerator(1);

        long prevId = generator.generateId();
        for (int i = 0; i < 1000; i++) {
            long id = generator.generateId();
            assertTrue(id > prevId, "IDs not strictly increasing");
            prevId = id;
        }
    }

    @Test
    void testConcurrency() throws InterruptedException {
        final IdGenerator generator = new IdGenerator(1);
        final Set<Long> ids = ConcurrentHashMap.newKeySet();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable generateTask = () -> {
            for (int i = 0; i < 1000; i++) {
                ids.add(generator.generateId());
            }
        };

        for (int i = 0; i < 10; i++) {
            executor.submit(generateTask);
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        assertEquals(10000, ids.size(), "Concurrency collision detected!");
    }

    @Test
    void testClockBackward() {
        IdGenerator generator = new IdGenerator(1) {
            private boolean firstCall = true;

            @Override
            protected long timestamp() {
                if (firstCall) {
                    firstCall = false;
                    return System.currentTimeMillis();
                } else {
                    return System.currentTimeMillis() - 10000;
                }
            }
        };

        generator.generateId();

        Exception exception = assertThrows(IllegalStateException.class, generator::generateId);
        assertTrue(exception.getMessage().contains("Clock moved backwards"));
    }
}
