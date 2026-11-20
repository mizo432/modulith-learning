package undecided.agiloom.requirementBacklog.internal;

/**
 * バックログアイテムがすでに別のフィーチャーに割り当てられている場合にスローされる例外。
 *
 * <p>バックログアイテムをフィーチャーに割り当てようとしたとき、
 * すでに他のフィーチャーに割り当てられている場合にこの例外をスローし、
 * プレゼンテーション層で HTTP 409 に変換します。
 */
class BacklogItemAlreadyAssignedToFeatureException extends IllegalArgumentException {

  BacklogItemAlreadyAssignedToFeatureException(String message) {
    super(message);
  }
}
