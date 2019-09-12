package prv.saevel.javacheck.retries;

public class RetryStrategies {

    private RetryStrategies(){
        ;
    }

    public static RetryStrategy neverRetry() {
        return retryLog -> false;
    }

    public static RetryStrategy alwaysRetry(){
        return retryLog -> true;
    }

    public static RetryStrategy maxRetries(int max){
        return retryLog -> (retryLog.size() <= max);
    }
}
