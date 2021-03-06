package com.rajendarreddyj.basics.concurrent.completablefuture;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author rajendarreddy
 *
 */
public class CompletableFutureTest {
    private static final Logger logger = Logger.getAnonymousLogger();
    @Test
    public void whenRunningCompletableFutureAsynchronously_thenGetMethodWaitsForResult() throws InterruptedException, ExecutionException {
        Future<String> completableFuture = this.calculateAsync();
        String result = completableFuture.get();
        Assert.assertEquals("Hello", result);
    }

    public Future<String> calculateAsync() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.complete("Hello");
            return null;
        });
        return completableFuture;
    }

    @Test
    public void whenRunningCompletableFutureWithResult_thenGetMethodReturnsImmediately() throws InterruptedException, ExecutionException {
        Future<String> completableFuture = CompletableFuture.completedFuture("Hello");
        String result = completableFuture.get();
        Assert.assertEquals("Hello", result);
    }

    public Future<String> calculateAsyncWithCancellation() throws InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        Executors.newCachedThreadPool().submit(() -> {
            Thread.sleep(500);
            completableFuture.cancel(false);
            return null;
        });
        return completableFuture;
    }

    @Test(expected = CancellationException.class)
    public void whenCancelingTheFuture_thenThrowsCancellationException() throws ExecutionException, InterruptedException {
        Future<String> future = this.calculateAsyncWithCancellation();
        future.get();
    }

    @Test
    public void whenCreatingCompletableFutureWithSupplyAsync_thenFutureReturnsValue() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
        Assert.assertEquals("Hello", future.get());
    }

    @Test
    public void whenAddingThenAcceptToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Void> future = completableFuture.thenAccept(s -> logger.info("Computation returned: " + s));
        future.get();
    }

    @Test
    public void whenAddingThenRunToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<Void> future = completableFuture.thenRun(() -> logger.info("Computation finished."));
        future.get();
    }

    @Test
    public void whenAddingThenApplyToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture.thenApply(s -> s + " World");
        Assert.assertEquals("Hello World", future.get());
    }

    @Test
    public void whenUsingThenCompose_thenFuturesExecuteSequentially() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello")
                .thenCompose(s -> CompletableFuture.supplyAsync(() -> s + " World"));
        Assert.assertEquals("Hello World", completableFuture.get());
    }

    @Test
    public void whenUsingThenCombine_thenWaitForExecutionOfBothFutures() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello").thenCombine(CompletableFuture.supplyAsync(() -> " World"),
                (s1, s2) -> s1 + s2);
        Assert.assertEquals("Hello World", completableFuture.get());
    }

    @Test
    public void whenUsingThenAcceptBoth_thenWaitForExecutionOfBothFutures() throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> "Hello").thenAcceptBoth(CompletableFuture.supplyAsync(() -> " World"), (s1, s2) -> logger.info(s1 + s2));
    }

    @Test
    public void whenFutureCombinedWithAllOfCompletes_thenAllFuturesAreDone() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Beautiful");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "World");
        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future1, future2, future3);
        // ...
        combinedFuture.get();
        Assert.assertTrue(future1.isDone());
        Assert.assertTrue(future2.isDone());
        Assert.assertTrue(future3.isDone());
        String combined = Stream.of(future1, future2, future3).map(CompletableFuture::join).collect(Collectors.joining(" "));
        Assert.assertEquals("Hello Beautiful World", combined);
    }

    @SuppressWarnings("unused")
    @Test
    public void whenFutureThrows_thenHandleMethodReceivesException() throws ExecutionException, InterruptedException {
        String name = null;
        // ...
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            if (name == null) {
                throw new RuntimeException("Computation error!");
            }
            return "Hello, " + name;
        }).handle((s, t) -> s != null ? s : "Hello, Stranger!");
        Assert.assertEquals("Hello, Stranger!", completableFuture.get());
    }

    @Test(expected = ExecutionException.class)
    public void whenCompletingFutureExceptionally_thenGetMethodThrows() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        // ...
        completableFuture.completeExceptionally(new RuntimeException("Calculation failed!"));
        // ...
        completableFuture.get();
    }

    @Test
    public void whenAddingThenApplyAsyncToFuture_thenFunctionExecutesAfterComputationIsFinished() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> future = completableFuture.thenApplyAsync(s -> s + " World");
        Assert.assertEquals("Hello World", future.get());
    }
}