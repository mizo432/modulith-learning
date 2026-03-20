package undecided.erp.common.uuidV7Provider;

import static undecided.shared.common.precondition.ObjectPrecondition.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.NonNull;
import undecided.shared.common.precondition.IndexedRuntimeException;

/**
 * RollingUuidV7IdProviderは、事前定義されたUUIDリストを基に、UUIDバージョン7を循環的に提供するプロバイダークラスです。
 *
 * <p>このクラスは、内部に保持するUUIDリストを順番に利用し、リストの末尾に到達した際には再び先頭に戻る形でUUIDを提供します。
 * 主に、テストや特定のUUIDの順序が必要なシナリオで利用することを目的としています。
 *
 * <p>特徴: - 一度初期化されると、固定されたUUIDリストに基づきUUIDを生成します。 - 内部リストが循環アクセス可能であるため、与えられたUUIDを無限に利用可能です。 -
 * スレッドセーフな実装により、複数スレッド環境下で安全に利用可能です。
 *
 * <p>このクラスはUuidV7Providerを拡張しており、デフォルトのプロバイダー動作をオーバーライドした機能を提供します。
 */
public class RollingUuidV7IdProvider extends UuidV7Provider {

  /**
   * 固定されたUUID文字列のリストを保持するためのリスト。
   *
   * <p>このリストは、UUID文字列を管理するために使用されます。 主に、指定された順序でUUIDを提供または参照するための内部ストレージとして機能します。
   *
   * <p>主な特徴: - 不変性: このリストはfinalであるため、参照そのものは変更されません。ただし、リストへの追加や削除は可能です。 - 逐次的アクセス:
   * このリストを元にUUIDを順に取得するロジックと連携します。
   *
   * <p>スレッドセーフな操作が必要な場合は、適切な同期メカニズムを併用する必要があります。
   */
  private final List<String> idList = new ArrayList<>();

  private int index;

  /**
   * RollingUuidV7IdProviderクラスのプライベートコンストラクタ。
   *
   * <p>指定されたUUIDのコレクションを受け取り、内部リストに追加します。 このコンストラクタは、クラス内部またはフレンドクラスからのみアクセス可能であり、
   * 主に初期化時のデータ設定に利用されます。
   *
   * @param idList UUID文字列のコレクション。空のコレクションやnull値を含むコレクションは許容されません。
   */
  private RollingUuidV7IdProvider(Collection<String> idList) {
    this.idList.addAll(idList);
    index = 0;
  }

  /**
   * 指定されたUUIDを使用して、カスタムUUIDバージョン7プロバイダーを初期化します。
   *
   * <p>UUIDリストが空またはnullを含む場合は例外をスローします。
   *
   * @param uuids 初期化に使用するUUIDの配列。空であってはならず、すべての要素が非nullである必要があります。
   * @throws IllegalArgumentException UUIDの配列が空である場合、またはnullを含む場合
   * @throws IndexedRuntimeException 配列内の特定のインデックスにnullが存在する場合
   */
  public static void initialize(@NonNull String... uuids) {
    checkNotNull(uuids, () -> new NullPointerException("uuids must not be null."));
    checkNotEmpty(uuids, () -> new IllegalArgumentException("UUIDs must not be empty"));
    checkAllElementNotNull(
        uuids,
        (index) ->
            new IndexedRuntimeException(
                index, new IllegalArgumentException("UUIDs must not contain null")));
    new UuidV7Provider(new RollingUuidV7IdProvider(Arrays.asList(uuids)));
  }

  /**
   * UUIDバージョン7プロバイダーの状態をリセットします。
   *
   * <p>このメソッドを呼び出すと、現在のUUIDバージョン7プロバイダーのインスタンスが初期状態に戻ります。 デフォルトのUUID生成ロジックが再び使用されるようになります。
   *
   * <p>内部的に{@link UuidV7Provider#clear()}メソッドを呼び出して、プロバイダーの状態をクリアします。
   */
  public static void clear() {
    UuidV7Provider.clear();
  }

  /**
   * 内部的にUUIDの新しいインスタンスを生成して返します。
   *
   * <p>このメソッドはスレッドセーフであり、提供されたUUIDリストから順番にUUIDを選択し、リストの末尾に到達すると再度先頭に戻ります。
   * 循環的なアクセスを行い、リストに基づくUUIDの提供を保証します。
   *
   * @return 選択されたリスト内のUUIDの新しいインスタンス
   */
  @Override
  protected synchronized @NonNull UUID internalNewInstance() {

    UUID result = UUID.fromString(idList.get(index));
    index++;
    if (index >= idList.size()) {
      index = 0;
    }
    return result;
  }
}
