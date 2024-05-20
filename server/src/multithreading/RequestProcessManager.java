package multithreading;

import network.Request;
import network.Response;
import network.Server;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class RequestProcessManager {

    private Request request;
    private Future<Response> responseFuture;
    private Response response;

    private boolean processed = false;

    public RequestProcessManager(Request request) {
        this.request = request;

        Callable<Response> callResponse = () -> Server.getInstance().makeResponse(this.request);

        responseFuture = ThreadManager.getExecuteRequestExecutor().submit(callResponse);

    }

    public boolean tryProcess() throws ExecutionException, InterruptedException {

        if (responseFuture.isDone()) {
            response = responseFuture.get();

            Runnable sendResponse = () -> Server.getInstance().sendResponse(response);

            ThreadManager.getSendResponseExecutor().execute(sendResponse);

            this.processed = true;
            return true;
        }

        return false;
    }

    public boolean isProcessed() {
        return processed;
    }
}
