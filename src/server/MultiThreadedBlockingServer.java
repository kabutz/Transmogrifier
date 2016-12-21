package server;

import handler.*;

import java.io.*;
import java.net.*;

public class MultiThreadedBlockingServer {
  public static void main(String... args) throws IOException {
    ServerSocket ss = new ServerSocket(8080);
    Handler<Socket, IOException> handler =
        new ThreadedHandler<>(
            new PrintingHandler<>(
                new TransmogrifyHandler()
            ));
    while (true) {
      Socket s = ss.accept(); // never null - blocks
      handler.handle(s);
    }
  }
}