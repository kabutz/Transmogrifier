package server;

import handler.*;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.concurrent.*;

public class BlockingNIOServer {
  public static void main(String... args) throws IOException {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));

    ExecutorService pool = new ThreadPoolExecutor(
        10, 100,
        0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<Runnable>(1000));
    Handler<SocketChannel, IOException> handler =
        new ExecutorServiceHandler<>(
            pool,
            new PrintingHandler<>(
                new BlockingChannelHandler(
                    new TransmogrifyChannelHandler())));
    while (true) {
      SocketChannel sc = ssc.accept(); // never null - blocks
      handler.handle(sc);
    }
  }
}