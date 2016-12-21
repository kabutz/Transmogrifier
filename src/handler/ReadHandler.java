package handler;

import handler.*;
import util.*;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class ReadHandler implements Handler<SelectionKey, IOException> {
  private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

  public ReadHandler(Map<SocketChannel, Queue<ByteBuffer>> pendingData) {
    this.pendingData = pendingData;
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
      Util.transmogrify(buf);
      pendingData.get(sc).add(buf);
      key.interestOps(SelectionKey.OP_WRITE);
    }
  }
}
