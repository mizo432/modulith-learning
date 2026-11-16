package undecided.agiloom.requirementBacklog.internal;

/**
 * バックログアイテムに対する操作権限がない場合にスローされる例外。
 *
 * <p>バックログアイテムの状態変更は、そのチームのSMとPOのみが行えます。
 * それ以外の従業員（一般メンバー、ステークホルダー）が状態変更を試みた場合にこの例外をスローし、
 * プレゼンテーション層で HTTP 403 に変換します。
 */
class BacklogItemPermissionDeniedException extends RuntimeException {

  BacklogItemPermissionDeniedException(String message) {
    super(message);
  }
}
