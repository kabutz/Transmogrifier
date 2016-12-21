package handler;

import java.io.*;
import java.nio.channels.*;

public class BlockingChannelHandler
    extends DecoratingHandler<SocketChannel, IOException> {

  public BlockingChannelHandler(Handler<SocketChannel, IOException> other) {
    super(other);
  }

  public void handle(SocketChannel sc) throws IOException {
    while(sc.isConnected()) {
      super.handle(sc);
    }
  }
}
