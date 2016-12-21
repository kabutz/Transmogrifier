package server;

import handler.*;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.util.*;

public class SingleThreadedPollingNonBlockingServer {
  public static void main(String... args) throws IOException {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));
    ssc.configureBlocking(false);

    Handler<SocketChannel, IOException> handler =
        new ExceptionHandler<>(
            new TransmogrifyChannelHandler());

    Collection<SocketChannel> sockets = new ArrayList<>();
    while (true) {
      SocketChannel newSocket = ssc.accept(); // mostly null - never blocks
      if (newSocket != null) {
        sockets.add(newSocket);
        System.out.println("Connected to " + newSocket);
        newSocket.configureBlocking(false);
      }

      for (Iterator<SocketChannel> it = sockets.iterator(); it.hasNext(); ) {
        SocketChannel sc = it.next();
        if (sc.isConnected()) {
          handler.handle(sc);
        } else {
          it.remove();
        }
      }
    }
  }
}