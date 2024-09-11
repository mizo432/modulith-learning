package undecided.erp.shared.crud;

import static undecided.erp.common.primitive.Lists2.newArrayList;
import static undecided.erp.common.primitive.Objects2.defaultIfNull;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import undecided.erp.common.annotation.VisibleForFramework;

/**
 * CrudRecordクラスは、CRUDタイプを持つレコードを表します。
 *
 * @param <T> レコードオブジェクトの型。
 */
@Getter
public class CrudRecord<T> {

  /**
   * CrudRecordオブジェクトのデフォルトのCRUDタイプ。
   */
  private static final CrudType DEFAULT_CRUD_TYPE = CrudType.NO_CHANGED;

  /**
   * CrudRecordインスタンス内のレコードのCRUDタイプを表します。
   */
  private final CrudType crudType;
  /**
   * レコード変数を表します。
   */
  private final T record;

  @VisibleForFramework
  public CrudRecord(T record, CrudType crudType) {
    this.crudType = crudType;
    this.record = record;
  }

  /**
   * 与えられたレコードとcrudTypeを使って新しいCrudRecordインスタンスを作成します。
   *
   * @param record CrudRecordのためのレコードオブジェクトです。
   * @param crudType CrudRecordのためのCrudTypeです。
   * @param <T> レコードオブジェクトの型です。
   * @return 作成されたCrudRecordインスタンスを返します。
   */
  public static <T> CrudRecord<T> create(@NonNull T record, CrudType crudType) {
    return new CrudRecord<>(record, crudType);

  }

  /**
   * 指定されたレコードとデフォルトのCrudTypeでデフォルトのCrudRecordを作成します。
   *
   * @param record CrudRecordのためのレコードオブジェクト。
   * @param <T> レコードオブジェクトの型。
   * @return 作成したデフォルトのCrudRecord。
   */
  public static <T> CrudRecord<T> createDefaultCrudRecord(@NonNull T record) {
    return create(record, DEFAULT_CRUD_TYPE);

  }

  /**
   * CrudRecordsクラスは、CRUDレコードのコレクションを表します。
   *
   * @param <T> レコードオブジェクトの型。
   */
  public static class CrudRecords<T> {

    /**
     * records変数はCRUDレコードのコレクションを表します。
     */
    private final List<CrudRecord<T>> records = newArrayList();

    /**
     * CrudRecordsの新しいインスタンスを生成します。
     *
     * @param records CRUDレコード
     */
    private CrudRecords(List<CrudRecord<T>> records) {
      this.records.addAll(defaultIfNull(records, newArrayList()));
    }

    /**
     * 変更されたレコードのリストを返します。
     *
     * @return 変更されたレコードのリスト
     */
    public List<T> changed() {
      return records
          .stream()
          .filter(CrudRecord::isChanged)
          .map(crudRecord -> crudRecord.record)
          .toList();
    }

    /**
     * 作成されたレコードのリストを返します。
     *
     * @return 作成されたレコードのリスト
     */
    public List<T> created() {
      return records
          .stream()
          .filter(CrudRecord::isCreated)
          .map(crudRecord -> crudRecord.record).toList();
    }

    /**
     * 変更されたレコードのリストを返します。
     *
     * @return 変更されたレコードのリスト
     */
    public List<T> updated() {
      return records
          .stream()
          .filter(CrudRecord::isUpdated)
          .map(crudRecord -> crudRecord.record).toList();
    }

    /**
     * 削除されたレコードのリストを返します。
     *
     * @return 削除されたレコードのリスト。
     */
    public List<T> deleted() {
      return records
          .stream()
          .filter(CrudRecord::isDeleted)
          .map(crudRecord -> crudRecord.record)
          .toList();
    }

    /**
     * 利用可能なレコードのリストを返します。
     *
     * @return 利用可能なレコードのリスト
     */
    public List<T> getAvailableRecords() {
      return records
          .stream()
          .filter(CrudRecord::available)
          .map(crudRecord -> crudRecord.record)
          .toList();
    }

    /**
     * レコードのリストを返します。
     *
     * @return レコードのリスト
     */
    public List<CrudRecord<T>> records() {
      return Collections.unmodifiableList(records);
    }

    /**
     * CrudRecordsクラスの新しいインスタンスを作成します。
     *
     * @param <T> レコードオブジェクトのタイプ
     * @param records CRUDレコード
     * @return CrudRecordsの新しいインスタンス
     */
    public static <T> CrudRecords<T> of(List<CrudRecord<T>> records) {
      return new CrudRecords<>(records);

    }

  }

  private boolean isChanged() {
    return crudType.isChanged();
  }

  /**
   * CrudRecordが「作成済み」状態であるかどうかを確認します。
   *
   * @return CrudRecordが「作成済み」状態である場合はtrue、それ以外の場合はfalseを返します。
   */
  private boolean isCreated() {
    return crudType.isCreated();
  }

  /**
   * CrudRecordが更新されたかどうかを確認します。
   *
   * @return CrudRecordが更新されていればtrue、そうでなければfalse。
   */
  private boolean isUpdated() {
    return crudType.isUpdated();
  }

  /**
   * CrudRecordが削除としてマークされているかどうかをチェックします。
   *
   * @return CrudRecordが削除としてマークされている場合はtrue、それ以外の場合はfalse。
   */
  private boolean isDeleted() {
    return crudType.isDeleted();
  }

  /**
   * CrudRecordが削除としてマークされているかどうかをチェックします。
   *
   * @return CrudRecordが削除としてマークされている場合はtrue、それ以外の場合はfalse。
   */
  private boolean available() {
    return crudType.available();
  }

}
