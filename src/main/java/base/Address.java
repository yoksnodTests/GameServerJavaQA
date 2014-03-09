package base;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Address {
    private static AtomicInteger subscriberIdGenerator = new AtomicInteger();
    private AtomicBoolean threadUsed = new AtomicBoolean();
    private final int subscriberId;

    public Address() {
        subscriberId = subscriberIdGenerator.incrementAndGet();
    }

    public int hashCode() {
        return subscriberId;
    }

    public boolean isThreadUsed() {
        return threadUsed.get();
    }

    public void setThreadUsed(boolean threadUsed) {
        this.threadUsed.set(threadUsed);
    }
}
