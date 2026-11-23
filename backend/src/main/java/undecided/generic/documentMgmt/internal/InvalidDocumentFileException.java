package undecided.generic.documentMgmt.internal;

/** 帳票ファイルが検証に合格しないことを表す例外。 */
class InvalidDocumentFileException extends RuntimeException {

  InvalidDocumentFileException(String message) {
    super(message);
  }

  InvalidDocumentFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
