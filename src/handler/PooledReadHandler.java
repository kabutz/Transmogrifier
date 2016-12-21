package handler;

import util.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.*;

public class PooledReadHandler implements Handler<SelectionKey, IOException> {
  private final ExecutorService pool;
  private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;
  private final Queue<Runnable> selectorActions;

  public PooledReadHandler(ExecutorService pool,
                           Map<SocketChannel, Queue<ByteBuffer>> pendingData,
                           Queue<Runnable> selectorActions) {
    this.pool = pool;
    this.pendingData = pendingData;
    this.selectorActions = selectorActions;
  }

  public void handle(SelectionKey key) throws IOException {
    SocketChannel sc = (SocketChannel) key.channel();
    ByteBuffer buf = ByteBuffer.allocateDirect(80);
    int read = sc.read(buf);
    if (read == -1) {
      pendingData.remove(sc);
      return;
    }
    if (read > 0) {
      pool.submit(() -> {
        Util.transmogrify(buf);
        pendingData.get(sc).add(buf);
        selectorActions.add(() -> key.interestOps(SelectionKey.OP_WRITE));
        key.selector().wakeup();
      });
    }
  }
}
