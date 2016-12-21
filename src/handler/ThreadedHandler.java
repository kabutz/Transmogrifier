package handler;

import java.util.function.*;

public class ThreadedHandler<S,X extends Throwable> extends ExceptionHandler<S,X> {
  public ThreadedHandler(Handler<S, X> other) {
    super(other);
  }

  public ThreadedHandler(Handler<S, X> other, BiConsumer<S, Throwable> exceptionConsumer) {
    super(other, exceptionConsumer);
  }

  public void handle(S s) {
    new Thread(() -> super.handle(s)).start();
  }
}
