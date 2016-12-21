package handler;

import util.*;

import java.io.*;
import java.net.*;

public class TransmogrifyHandler implements Handler<Socket, IOException> {
  public void handle(Socket s) throws IOException {
    try (
        InputStream in = s.getInputStream();
        OutputStream out = s.getOutputStream()
    ) {
      int data;
      while ((data = in.read()) != -1) {
        out.write(Util.transmogrify(data));
      }
    }
  }
}
