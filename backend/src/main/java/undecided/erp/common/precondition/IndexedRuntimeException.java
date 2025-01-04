package undecided.erp.common.precondition;

import lombok.Getter;

/**
 * 発生したエラーに関連するインデックスの追加コンテキストを含むカスタム実行時例外です。
 * <p>
 * この例外は、コレクションやその他のインデックス付きデータに関連するエラーを処理する際に便利で、 例外メッセージや処理に特定のインデックス情報を含められます。
 */
@Getter
public class IndexedRuntimeException extends RuntimeException {

  private final Integer index;

  /**
   * Constructs a new {@code IndexedRuntimeException} with the specified index and cause.
   *
   * @param index the index associated with the error context
   * @param cause the underlying cause of the exception
   */
  public IndexedRuntimeException(Integer index, Throwable cause) {
    super(cause);
    this.index = index;
  }

  /**
   * Constructs a new {@code IndexedRuntimeException} with the specified message and index.
   *
   * @param message the detail message describing the exception
   * @param index the index associated with the error context
   */
  public IndexedRuntimeException(String message, Integer index) {
    super(String.format("Index:%d %s", index, message));
    this.index = index;
  }

  /**
   * Constructs a new {@code IndexedRuntimeException} with the specified message, index, and cause.
   *
   * @param message the detail message describing the exception
   * @param index the index associated with the error context
   * @param cause the underlying cause of the exception
   */
  public IndexedRuntimeException(String message, Integer index, Throwable cause) {
    super(String.format("Index:%d %s", index, message), cause);
    this.index = index;
  }
}
