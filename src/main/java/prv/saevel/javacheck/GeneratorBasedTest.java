package prv.saevel.javacheck;

import org.junit.Assert;
import prv.saevel.javacheck.generators.Generator;
import prv.saevel.javacheck.retries.Retries;
import prv.saevel.javacheck.retries.RetryStrategy;

import java.time.Duration;
import java.util.function.Consumer;

public interface GeneratorBasedTest {

    Duration dataGenerationTimeout();

    RetryStrategy retryStrategy();

    default <X> void forAll(Generator<X> generator, Consumer<X> testBody){
        try {
            testBody.accept(Retries.retry(generator.sample(), dataGenerationTimeout(), retryStrategy()).get());
        } catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
