package prv.saevel.javacheck.retries;

import java.util.List;

public interface RetryStrategy {

    boolean shouldRetry(List<RetryRecord> retryLog);
}
