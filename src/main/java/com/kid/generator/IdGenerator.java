package com.kid.generator;

public class IdGenerator {

    private final static long EPOCH = 1700000000000L; // example epoch (pick your own recent timestamp)

    private final static long NODE_ID_BITS = 10L;
    private final static long MAX_NODE_ID = ~(-1L << NODE_ID_BITS);

    private final static long SEQUENCE_BITS = 12L;
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    private final static long NODE_ID_SHIFT = SEQUENCE_BITS;
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + NODE_ID_BITS;

    private final long nodeId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public IdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException(String.format("Node ID must be between %d and %d", 0, MAX_NODE_ID));
        }
        this.nodeId = nodeId;
    }

    public synchronized long generateId() {
        long currentTimestamp = timestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards. Refusing to generate ID.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT) |
                (nodeId << NODE_ID_SHIFT) |
                sequence;
    }

    private long waitNextMillis(long currentMillis) {
        while (currentMillis <= lastTimestamp) {
            currentMillis = timestamp();
        }
        return currentMillis;
    }

    protected long timestamp() {
        return System.currentTimeMillis();
    }
}

