package util;

import java.nio.*;

public class Util {
  public static int transmogrify(int data) {
    return Character.isLetter(data) ? data ^ ' ' : data;
  }

  public static void transmogrify(ByteBuffer buf) {
    // before reading "hello": pos = 0 limit = 80 capacity = 80
    // after reading "hello": pos = 5 limit = 80 capacity = 80
    // set pos = 0 and limit = 5
    buf.flip();
    for (int i = 0; i < buf.limit(); i++) {
      buf.put(i, (byte) transmogrify(buf.get(i)));
    }
  }
}
