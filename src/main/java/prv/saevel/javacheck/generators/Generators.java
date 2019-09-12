package prv.saevel.javacheck.generators;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generators {

    private Generators(){
        ;
    }

    private static final String ALPHA_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

    public static final Generator<Integer> intGenerator(){
        Random random = new Random();
        return pure(random.nextInt());
    }

    public static final Generator<Long> longGenerator(){
        Random random = new Random();
        return pure(random.nextLong());
    }

    public static final Generator<Double> doubleGenerator(){
        Random random = new Random();
        return pure(random.nextDouble());
    }

    public static final Generator<Boolean> booleanGenerator(){
        Random random = new Random();
        return pure(random.nextBoolean());
    }

    public static final Generator<Short> shortGenerator(){
        Random random = new Random();
        return pure((short) random.nextInt(Short.MAX_VALUE + 1));
    }

    public static final Generator<Byte> byteGenerator(){
        Random random = new Random();
        byte[] bytes = new byte[1];
        random.nextBytes(bytes);
        return pure(bytes[0]);
    }

    public static final Generator<byte[]> byteArrayGenerator(int size){
        Random random = new Random();
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        return pure(bytes);
    }

    public static final Generator<Character> alphaChar(){
        return range(0, ALPHA_STR.length()).map(ALPHA_STR::charAt);
    }

    public static final Generator<String> alphaStr(int size){
        if(size >= 0) {
            throw new IllegalArgumentException("The size of a String must be positive. Actual value: " + size);
        } else {
            return streamOf(alphaChar()).map(charStream -> charStream.map(c -> c.toString()).collect(Collectors.joining()));
        }
    }

    public static final<T> Generator<T> pure(T t){
        return () -> CompletableFuture.completedFuture(t);
    }

    public static final Generator<Integer> range(int min, int max){
        return intGenerator().map(i -> i + min).map(i -> i % max);
    }

    public static final Generator<Long> range(long min, long max){
        return longGenerator().map(i -> i + min).map(i -> i % max);
    }

    public static final<T> Generator<Stream<T>> streamOf(Generator<T> basicGenerator){
        return () -> Stream.generate(basicGenerator::sample).reduce(
                CompletableFuture.completedFuture(Stream.<T>empty()),
                (futureStream, futureX) -> futureStream.thenCompose(stream -> futureX.thenApply(x -> Stream.concat(stream, Stream.of(x)))),
                (left, right) -> left.thenCompose(leftStream -> right.thenApply(rightStream -> Stream.concat(leftStream, rightStream)))
        );
    }

    public static final<T> Generator<Optional<T>> optional(Generator<T> basicGenerator){
        return booleanGenerator().flatMap(flag -> {
            if(flag){
                return basicGenerator.map(Optional::ofNullable);
            } else {
                return pure(Optional.empty());
            }
        });
    }

    public static final<T> Generator<T> oneOf(List<T> options){
        if(options.isEmpty()){
            throw new IllegalArgumentException("Options cannot be empty");
        } else {
            return intGenerator().map(Math::abs).map(i -> i % options.size()).map(options::get);
        }
    }

    public static final<T> Generator<T> oneOf(T... options){
        if(options.length == 0){
            throw new IllegalArgumentException("Options cannot be empty");
        } else {
            return intGenerator().map(Math::abs).map(i -> i % options.length).map(i -> options[i]);
        }
    }

    public static final<T> Generator<T> oneOf(Generator<T>... options){
        if(options.length == 0){
            throw new IllegalArgumentException("Options cannot be empty");
        } else {
            return intGenerator()
                    .map(Math::abs)
                    .map(i -> i % options.length)
                    .flatMap(i -> options[i]);
        }
    }
}
