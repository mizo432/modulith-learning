package undecided.erp.shared.crud;

public enum CrudType {
  NO_CHANGED, CREATED, UPDATED, DELETED;

  public boolean isDeleted() {
    return this == DELETED;
  }

  public boolean isUpdated() {
    return this == UPDATED;
  }

  public boolean isCreated() {
    return this == CREATED;
  }

  public boolean isChanged() {
    return this != NO_CHANGED;
  }

  /**
   * 現在のCrudTypeが利用可能かどうかを確認します。
   *
   * @return CrudTypeが利用可能な場合はtrue、そうでない場合はfalseを返します。
   */
  public boolean available() {
    return this != DELETED;
  }
}
