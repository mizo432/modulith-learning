package undecided.generic.documentMgmt.internal;

/** 帳票が見つからないことを表す例外。 */
class DocumentNotFoundException extends RuntimeException {

  DocumentNotFoundException(String message) {
    super(message);
  }
}
