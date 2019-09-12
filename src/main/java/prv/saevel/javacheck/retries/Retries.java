package prv.saevel.javacheck.retries;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Retries {

    private Retries(){
        ;
    }

    public static <X> Future<X> retry(Future<X> f, Duration timeout, RetryStrategy strategy){
        return retryInternal(f, LocalDateTime.now(), timeout, strategy, new LinkedList<>());
    }

    private static <X> Future<X> retryInternal(Future<X> f,
                                        LocalDateTime startTimestamp,
                                        Duration timeout,
                                        RetryStrategy retryStrategy,
                                        LinkedList<RetryRecord> retryLog){

        try {
            f.get(timeout.toMillis(), TimeUnit.MILLISECONDS);
            return f;
        } catch(Exception e) {

            LocalDateTime timestamp = LocalDateTime.now();
            LinkedList<RetryRecord> updatedRecords = ((LinkedList<RetryRecord>) retryLog.clone());
            updatedRecords.add(new RetryRecord(timestamp, e));

            if(retryStrategy.shouldRetry(updatedRecords)){
                return retryInternal(f, startTimestamp, timeout, retryStrategy, updatedRecords);
            } else {
                CompletableFuture<X> failedFuture = new CompletableFuture<>();
                failedFuture.completeExceptionally(e);
                return failedFuture;
            }
        }
    }
}
