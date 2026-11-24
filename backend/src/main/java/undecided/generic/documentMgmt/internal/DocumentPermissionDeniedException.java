package undecided.generic.documentMgmt.internal;

/** 帳票操作の権限がないことを表す例外。 */
class DocumentPermissionDeniedException extends RuntimeException {

  DocumentPermissionDeniedException(String message) {
    super(message);
  }
}
