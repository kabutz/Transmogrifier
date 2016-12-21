package server;

import handler.*;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ExecutorServiceBlockingServer {
  public static void main(String... args) throws IOException {
    ServerSocket ss = new ServerSocket(8080);
    ExecutorService pool = new ThreadPoolExecutor(
        10, 100,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(1000));
    Handler<Socket, IOException> handler =
        new ExecutorServiceHandler<Socket, IOException>(
            pool,
            new PrintingHandler<>(
                new TransmogrifyHandler()
            ));
    while (true) {
      Socket s = ss.accept(); // never null - blocks
      handler.handle(s);
    }
  }
}