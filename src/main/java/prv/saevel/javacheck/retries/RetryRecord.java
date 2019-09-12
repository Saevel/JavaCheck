package prv.saevel.javacheck.retries;

import java.time.LocalDateTime;

public class RetryRecord {

    private LocalDateTime timestamp;

    private Throwable reason;

    public RetryRecord(LocalDateTime timestamp, Throwable reason) {
        this.timestamp = timestamp;
        this.reason = reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Throwable getReason() {
        return reason;
    }
}