package handler;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

public class AcceptHandler implements Handler<SelectionKey, IOException> {
  private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

  public AcceptHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
    this.pendingData = pendingData;
  }

  public void handle(SelectionKey key) throws IOException {
    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
    SocketChannel sc = ssc.accept(); // never null, nonblocking
    System.out.println("Someone connected: " + sc);
    sc.configureBlocking(false);
    pendingData.put(sc, new ConcurrentLinkedQueue<>());

    sc.register(key.selector(), SelectionKey.OP_READ);
  }
}
