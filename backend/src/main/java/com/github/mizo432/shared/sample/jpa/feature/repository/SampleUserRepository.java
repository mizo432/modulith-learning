package com.github.mizo432.shared.sample.jpa.feature.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * CrudRepositoryの使用パターンを示すサンプルリポジトリです。
 *
 * <p>基本的なCRUD操作のみが必要な場合に使用します。 JpaRepositoryよりも軽量で、バッチ処理やページネーションの機能は不要な場面に適しています。
 */
public interface SampleUserRepository extends CrudRepository<SampleUser, SnowflakeId> {

  /**
   * メールアドレスでユーザーを検索します。
   *
   * @param email 検索するメールアドレス
   * @return 一致するユーザーが存在する場合、そのOptional
   */
  Optional<SampleUser> findByEmail(String email);

  /**
   * 名前とアクティブステータスでユーザーを検索します。
   *
   * @param name 名前
   * @param active アクティブステータス
   * @return 一致するユーザーが存在する場合、そのOptional
   */
  Optional<SampleUser> findByNameAndActive(String name, Boolean active);
}
