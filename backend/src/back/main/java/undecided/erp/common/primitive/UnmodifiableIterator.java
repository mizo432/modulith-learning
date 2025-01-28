package undecided.erp.common.primitive;

import com.google.errorprone.annotations.DoNotCall;
import java.util.Iterator;

public abstract class UnmodifiableIterator<E> implements Iterator<E> {

  protected UnmodifiableIterator() {
  }

  @Deprecated
  @Override
  @DoNotCall("Always throws UnsupportedOperationException")
  public final void remove() {
    throw new UnsupportedOperationException();
  }
}
