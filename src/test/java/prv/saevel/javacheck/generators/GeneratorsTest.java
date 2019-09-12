package prv.saevel.javacheck.generators;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

public class GeneratorsTest {

    @Test
    public void intGeneratorTest() throws ExecutionException, InterruptedException {
        Generators.intGenerator().sample().get();
    }

    @Test
    public void longGeneratorTest() throws Exception {
        Generators.longGenerator().sample().get();
    }

    @Test
    public void doubleGeneratorTest() throws Exception {
        Generators.doubleGenerator().sample().get();
    }

    @Test
    public void booleanGeneratorTest() throws Exception {
        Generators.booleanGenerator().sample().get();
    }

    @Test
    public void intRangeGeneratorTest() throws Exception {
        int value = Generators.range(0, 100).sample().get();

        Assert.assertTrue("Value " + value + " is not greater or equal to 0", value >= 0);
        Assert.assertTrue("Value " + value + " is not lesser or equal to 100", value <= 100);
    }

    @Test
    public void longRangeGeneratorTest() throws Exception {
        long value = Generators.range(0, 100).sample().get();

        Assert.assertTrue("Value " + value + " is not greater or equal to 0", value >= 0);
        Assert.assertTrue("Value " + value + " is not lesser or equal to 100", value <= 100);
    }
}
