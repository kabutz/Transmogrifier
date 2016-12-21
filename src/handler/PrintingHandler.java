package handler;

// a decorator
public class PrintingHandler<S, X extends Throwable> extends DecoratingHandler<S, X> {
  public PrintingHandler(Handler<S, X> other) {
    super(other);
  }

  public void handle(S s) throws X {
    System.out.println("Connected to " + s);
    try {
      super.handle(s);
    } finally {
      System.out.println("Disconnected from " + s);
    }
  }
}
