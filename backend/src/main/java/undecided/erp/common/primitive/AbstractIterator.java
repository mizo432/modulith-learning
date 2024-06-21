package undecided.erp.common.primitive;

import static undecided.erp.common.primitive.NullnessCasts.uncheckedCastNullableTToT;
import static undecided.erp.common.verifier.ObjectVerifiers.verifyState;

import java.util.NoSuchElementException;
import undecided.erp.common.annotation.CanIgnoreReturnValue;

public abstract class AbstractIterator<T> extends UnmodifiableIterator<T> {

  private State state = State.NOT_READY;

  protected AbstractIterator() {
  }

  private enum State {
    READY,

    NOT_READY,

    DONE,

    FAILED,
  }

  private T next;

  protected abstract T computeNext();

  @CanIgnoreReturnValue
  protected final T endOfData() {
    state = State.DONE;
    return null;
  }

  @Override
  public final boolean hasNext() {
    verifyState(state != State.FAILED,
        () -> new IllegalStateException("state is State.FAILED"));
    return switch (state) {
      case DONE -> false;
      case READY -> true;
      default -> tryToComputeNext();
    };
  }

  private boolean tryToComputeNext() {
    state = State.FAILED;
    next = computeNext();
    if (state != State.DONE) {
      state = State.READY;
      return true;
    }
    return false;
  }

  @CanIgnoreReturnValue
  @Override
  public final T next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    state = AbstractIterator.State.NOT_READY;
    T result = uncheckedCastNullableTToT(next);
    next = null;
    return result;
  }

  public final T peek() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return uncheckedCastNullableTToT(next);
  }
}
