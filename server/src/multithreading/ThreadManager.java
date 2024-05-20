package multithreading;

import network.Request;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ThreadManager {
    private static ExecutorService requestExecutor = Executors.newCachedThreadPool();
    private static ForkJoinPool executeRequestPool = ForkJoinPool.commonPool();
    private static ExecutorService sendResponseExecutor = Executors.newFixedThreadPool(5);
    private static ExecutorService listenResponseExecutor = Executors.newCachedThreadPool();

    public static ExecutorService getRequestExecutor() {
        return requestExecutor;
    }

    public static ForkJoinPool getExecuteRequestExecutor() {
        return executeRequestPool;
    }

    public static ExecutorService getSendResponseExecutor() {
        return sendResponseExecutor;
    }

    public static ExecutorService getListenResponseExecutor() {
        return listenResponseExecutor;
    }

    public static void processRequest(Request request) {

    }
}
