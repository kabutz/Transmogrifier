package handler;

import util.*;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class TransmogrifyChannelHandler implements Handler<SocketChannel, IOException> {
  public void handle(SocketChannel sc) throws IOException {
    ByteBuffer buf = ByteBuffer.allocateDirect(80);
    int read = sc.read(buf);
    if (read == -1) {
      sc.close();
      return;
    }
    if (read > 0) {
      Util.transmogrify(buf);
      while(buf.hasRemaining()) {
        sc.write(buf);
      }
    }
  }
}
