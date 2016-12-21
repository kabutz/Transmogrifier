package server;

import handler.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

public class SelectorNonBlockingServerWithWorkerPool {
  public static void main(String... args) throws IOException {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));
    ssc.configureBlocking(false);
    Selector selector = Selector.open();
    ssc.register(selector, SelectionKey.OP_ACCEPT);

    ExecutorService pool = Executors.newFixedThreadPool(10);
    Queue<Runnable> selectorActions = new ConcurrentLinkedQueue<>();

    Map<SocketChannel, Queue<ByteBuffer>> pendingData = new ConcurrentHashMap<>();
    Handler<SelectionKey, IOException> acceptHandler = new AcceptHandler(pendingData);
    Handler<SelectionKey, IOException> readHandler = new PooledReadHandler(pool, pendingData, selectorActions);
    Handler<SelectionKey, IOException> writeHandler = new WriteHandler(pendingData);

    while (true) {
      selector.select();
      processSelectorActions(selectorActions);
      Set<SelectionKey> keys = selector.selectedKeys();
      for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
        SelectionKey key = it.next();
        it.remove();
        if (key.isValid()) {
          if (key.isAcceptable()) {
            acceptHandler.handle(key);
          } else if (key.isReadable()) {
            readHandler.handle(key);
          } else if (key.isWritable()) {
            writeHandler.handle(key);
          }
        }
      }
    }
  }

  private static void processSelectorActions(Queue<Runnable> selectorActions) {
    Runnable action;
    while((action = selectorActions.poll()) != null) {
      action.run();
    }
  }
}