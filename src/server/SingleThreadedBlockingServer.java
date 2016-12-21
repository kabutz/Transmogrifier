package server;

import handler.*;

import java.io.*;
import java.net.*;

public class SingleThreadedBlockingServer {
  public static void main(String... args) throws IOException {
    ServerSocket ss = new ServerSocket(8080);
    Handler<Socket, IOException> handler =
        new ExceptionHandler<>(
            new PrintingHandler<>(
                new TransmogrifyHandler()
            ));
    while (true) {
      Socket s = ss.accept(); // never null - blocks
      handler.handle(s);
      s.close();
    }
  }
}