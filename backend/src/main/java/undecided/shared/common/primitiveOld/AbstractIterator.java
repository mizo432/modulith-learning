package undecided.shared.common.primitiveOld;

import static undecided.erp.common.precondition.ObjectPrecondition.checkState;
import static undecided.shared.common.primitiveOld.NullnessCasts.uncheckedCastNullableTToT;

import java.util.NoSuchElementException;
import undecided.erp.common.annotation.CanIgnoreReturnValue;

/**
 * An abstract base class to simplify the implementation of iterators that do not support
 * modifications. Subclasses must implement the {@link #computeNext()} method to define their
 * specific iteration logic.
 *
 * <p>This class manages the iteration state and provides helper methods for determining the next
 * element or signaling the end of data. Once the data is exhausted or the iteration fails,
 * subsequent calls to {@link #hasNext()} or {@link #next()} will respond appropriately.
 *
 * @param <T> the type of elements returned by this iterator
 */
public abstract class AbstractIterator<T> extends UnmodifiableIterator<T> {

  /**
   * Indicates the current state of the iterator during its operation.
   *
   * <ul>
   *   <li>{@code READY}: The next element is available for retrieval.
   *   <li>{@code NOT_READY}: The iterator has not attempted to compute the next element.
   *   <li>{@code DONE}: The iteration has completed, and no more elements are available.
   *   <li>{@code FAILED}: An error occurred during the computation of the next element.
   * </ul>
   *
   * This variable is used internally to manage the lifecycle and behavior of the iterator.
   */
  private State state = State.NOT_READY;

  /**
   * Holds the next element to be returned by the iterator. This variable is set during the
   * computation of the next element and is accessed when the next element is retrieved using the
   * {@link #next()} method. It temporarily stores the next valid element unless the iteration has
   * completed or failed, in which case it may be null.
   */
  private T next;

  /**
   * Constructs an instance of the AbstractIterator class.
   *
   * <p>This protected constructor ensures that the AbstractIterator class can only be instantiated
   * as part of its subclasses. It allows subclasses to manage and define their own specific
   * iteration logic by implementing required abstract methods.
   */
  protected AbstractIterator() {}

  /**
   * Computes the next element in the sequence. This method must be implemented by subclasses to
   * define the logic for producing the next element.
   *
   * @return the next element in the sequence, or {@code null} if there are no more elements
   */
  protected abstract T computeNext();

  /**
   * Marks the end of the data when the iteration is complete. This method transitions the internal
   * state to {@code State.DONE} and returns {@code null}.
   *
   * @return {@code null}, indicating that there are no more elements to iterate over.
   */
  @CanIgnoreReturnValue
  protected final T endOfData() {
    state = State.DONE;
    return null;
  }

  /**
   * Determines whether there are more elements in the iteration. This method evaluates the current
   * state of the iterator and attempts to compute the next element if necessary, returning whether
   * further iteration is possible.
   *
   * @return {@code true} if additional elements are available in the sequence; {@code false} if the
   *     iteration has completed or the state indicates failure.
   */
  @Override
  public final boolean hasNext() {
    checkState(state != State.FAILED, () -> new IllegalStateException("state is State.FAILED"));
    return switch (state) {
      case DONE -> false;
      case READY -> true;
      default -> tryToComputeNext();
    };
  }

  /**
   * Attempts to compute the next element in the iteration. This method sets the internal state to
   * {@code State.FAILED}, invokes the {@code computeNext} method to determine the next element, and
   * updates the state accordingly. If a new element is successfully computed, the state transitions
   * to {@code State.READY}.
   *
   * @return {@code true} if a next element has been successfully computed and is ready for
   *     iteration, {@code false} if the computation failed or the iteration has completed (state is
   *     {@code State.DONE}).
   */
  private boolean tryToComputeNext() {
    state = State.FAILED;
    next = computeNext();
    if (state != State.DONE) {
      state = State.READY;
      return true;
    }
    return false;
  }

  /**
   * Returns the next element in the iteration.
   *
   * <p>This method retrieves and returns the next available element from the iterator. If there are
   * no more elements to iterate over, it throws a {@code NoSuchElementException}. After retrieving
   * the next element, the state is updated, and the internal storage for the element is reset.
   *
   * @return the next element in the iteration
   * @throws NoSuchElementException if the iteration has no more elements
   */
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

  /**
   * Returns the next element in the iteration without advancing the iterator.
   *
   * <p>This method allows a peek at the next element in the sequence without altering the internal
   * state of the iterator. If the iteration has no more elements, it throws a {@code
   * NoSuchElementException}.
   *
   * @return the next element in the iteration
   * @throws NoSuchElementException if the iteration has no more elements
   */
  public final T peek() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return uncheckedCastNullableTToT(next);
  }

  /**
   * Represents the internal state of the iterator during iteration.
   *
   * <p>The {@code State} enum is used to manage the lifecycle of the iteration process within the
   * {@code AbstractIterator} class. It defines the following states:
   *
   * <ul>
   *   <li>{@code READY}: Indicates that the next element in the iteration is ready to be returned.
   *   <li>{@code NOT_READY}: Indicates that the next element is not yet computed, requiring further
   *       evaluation.
   *   <li>{@code DONE}: Indicates that the iteration has reached its end, and no more elements are
   *       available.
   *   <li>{@code FAILED}: Indicates that the iterator has encountered an error or unexpected
   *       condition.
   * </ul>
   *
   * <p>The internal logic of {@code AbstractIterator} transitions between these states to manage
   * and determine the availability of the next element and the end of the iteration.
   */
  private enum State {
    /**
     * Indicates that the next element in the iteration is ready to be returned.
     *
     * <p>This state is used within the iteration process to signal that a subsequent element is
     * available and will be provided upon request.
     */
    READY,

    /**
     * Indicates that the next element in the iteration is not ready and requires further
     * computation.
     *
     * <p>This state is used to show that the iterator is in the process of determining the next
     * element in the sequence. The {@code NOT_READY} state suggests that the iterator has not yet
     * decided if there is another element to return or if the iteration is complete.
     */
    NOT_READY,

    /**
     * Indicates that the iteration has reached its end and no further elements are available.
     *
     * <p>The {@code DONE} state is used to signal that the iterator has exhausted all elements and
     * the iteration process is complete. This state marks the termination point of the iteration,
     * ensuring that no additional calls will produce further elements.
     */
    DONE,

    /**
     * Indicates that the iterator has encountered an error or unexpected condition.
     *
     * <p>The {@code FAILED} state is used when an iteration process cannot be successfully
     * completed due to an exception or an invalid state. Once the iterator reaches this state,
     * further attempts to retrieve elements will not succeed. The specific cause of failure should
     * be handled elsewhere in the logic.
     */
    FAILED,
  }
}
