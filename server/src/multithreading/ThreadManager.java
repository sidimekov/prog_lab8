package multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ThreadManager {
    private static ExecutorService requestExecutor = Executors.newCachedThreadPool();
    private static ForkJoinPool executeRequestPool = ForkJoinPool.commonPool();
    private static ExecutorService sendResponseExecutor = Executors.newFixedThreadPool(5);

    public static ExecutorService getRequestExecutor() {
        return requestExecutor;
    }

    public static ForkJoinPool getExecuteRequestExecutor() {
        return executeRequestPool;
    }

    public static ExecutorService getSendResponseExecutor() {
        return sendResponseExecutor;
    }
}
