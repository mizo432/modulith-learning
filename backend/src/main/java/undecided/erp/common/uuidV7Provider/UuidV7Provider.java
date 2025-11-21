package undecided.erp.common.uuidV7Provider;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedEpochGenerator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.NonNull;

/**
 * UUIDを生成するためのプロバイダークラス。 主にUUIDバージョン7の生成処理に関連する機能を提供する。
 *
 * <p>このクラスはスレッドセーフであり、単一インスタンスとして動作する。 内部的にAtomicReferenceを使用して、UUID生成のコンテキストを保持している。
 *
 * <p>機能: - UUIDのインスタンスを生成するためのメソッドを公開。 - インスタンスの管理とスレッドセーフな操作を可能にする設計。
 */
public class UuidV7Provider {

  private static final TimeBasedEpochGenerator GENERATOR;

  /**
   * UUIDバージョン7のプロバイダーインスタンスをスレッドセーフに管理するためのAtomicReference。
   *
   * <p>この変数は、UUID生成のためのコンテキストを保持し、スレッド間で安全に共有される。 主にスレッドセーフな形でプロバイダーの差し替えや参照を行うことができるよう設計されている。
   *
   * <p>初期値として、新しいUuidV7Providerインスタンスを持つ。
   */
  private static final AtomicReference<UuidV7Provider> UUID_V7_PROVIDER_ATOMIC_REFERENCE =
      new AtomicReference<>(new UuidV7Provider());

  static {
    GENERATOR = Generators.timeBasedEpochGenerator();
  }

  /**
   * UuidV7Providerクラスのデフォルトコンストラクタ。
   *
   * <p>このコンストラクタは、クラス外部から直接アクセスすることを制限するためprotectedスコープとされている。
   * UuidV7Providerクラスのインスタンス化は、主にクラス内またはサブクラスから行われることを想定している。
   *
   * <p>主な特徴: - クラス内で静的に管理されるプロバイダーインスタンスの初期化に利用される。 - スレッドセーフなUUID生成の基盤を提供する。
   */
  protected UuidV7Provider() {}

  /**
   * 指定されたUuidV7Providerインスタンスを使用して、 静的に管理されるUUIDバージョン7プロバイダーを更新するコンストラクタ。
   *
   * <p>内部的にAtomicReferenceを使用してインスタンスをスレッドセーフに設定する。
   *
   * @param uuidV7Provider 新しく設定するUuidV7Providerインスタンス。 このインスタンスをUUIDバージョン7プロバイダーとして使用する。
   */
  protected UuidV7Provider(@NonNull UuidV7Provider uuidV7Provider) {
    UUID_V7_PROVIDER_ATOMIC_REFERENCE.set(uuidV7Provider);
  }

  /**
   * UUIDバージョン7の新しいインスタンスを生成して返します。
   *
   * <p>このメソッドはスレッドセーフであり、内部的に管理された UUIDバージョン7プロバイダーを使用してUUIDを生成します。
   *
   * @return 新しく生成されたUUIDのインスタンス
   */
  public static @NonNull UUID newInstance() {
    return UUID_V7_PROVIDER_ATOMIC_REFERENCE.get().internalNewInstance();
  }

  public static void clear() {
    UUID_V7_PROVIDER_ATOMIC_REFERENCE.set(new UuidV7Provider());
  }

  /**
   * UUIDバージョン7の新しいインスタンスを生成して返します。
   *
   * <p>このメソッドは {@link UuidV7Provider} クラスやそのサブクラス内で使用され、UUID生成ロジックを提供します。
   * スレッドセーフな方法でUUIDを生成することを目的とし、内部的に設定されたUUID生成器を用いて新しいUUIDを作成します。
   *
   * @return 新しく生成されたUUIDオブジェクト
   */
  protected @NonNull UUID internalNewInstance() {
    return GENERATOR.generate();
  }
}
