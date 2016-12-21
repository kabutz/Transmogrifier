package handler;

public abstract class DecoratingHandler<S, X extends Throwable> implements Handler<S, X> {
  private final Handler<S,X> other;

  public DecoratingHandler(Handler<S, X> other) {
    this.other = other;
  }

  public void handle(S s) throws X {
    other.handle(s);
  }
}
