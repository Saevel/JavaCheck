package prv.saevel.javacheck.generators;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Generator<X> {

    public CompletableFuture<X> sample();

    default<Y> Generator<Y> map(Function<X, Y> f) {
        return () -> this.sample().thenApply(f::apply);
    }

    default<Y> Generator<Y> flatMap(Function<X, Generator<Y>> f) {
        return () -> this.sample().thenCompose(x -> f.apply(x).sample());
    }
}
