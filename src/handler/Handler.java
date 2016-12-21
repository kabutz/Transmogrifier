package handler;

import java.io.*;

public interface Handler<S, X extends Throwable> {
  public void handle(S s) throws X;
}
