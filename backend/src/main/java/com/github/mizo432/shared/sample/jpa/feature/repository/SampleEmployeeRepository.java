package com.github.mizo432.shared.sample.jpa.feature.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * JpaRepositoryの使用パターンを示すサンプルリポジトリです。
 *
 * <p>バッチ処理やページネーションが必要な場合に使用します。 CrudRepositoryに追加して、saveAll、deleteAll、ページネーションなどの機能を提供します。
 */
@Repository
public interface SampleEmployeeRepository extends JpaRepository<SampleEmployee, SnowflakeId> {

  /**
   * 名前に指定した文字列を含む従業員を検索します（部分一致）。
   *
   * @param keyword 検索キーワード
   * @return 一致する従業員リスト
   */
  List<SampleEmployee> findByNameContaining(String keyword);

  /**
   * 部署で従業員を検索します。
   *
   * @param department 部署名
   * @return 一致する従業員リスト
   */
  List<SampleEmployee> findByDepartment(String department);

  /**
   * 入日期が指定された日付範囲内の従業員を検索します。
   *
   * @param from 開始日
   * @param to 終了日
   * @return 一致する従業員リスト
   */
  List<SampleEmployee> findByHireDateBetween(LocalDate from, LocalDate to);

  /**
   * 名前と部署の両方で部分一致検索します。
   *
   * @param nameKeyword 名前キーワード
   * @param departmentKeyword 部署キーワード
   * @return 一致する従業員リスト
   */
  List<SampleEmployee> findByNameContainingAndDepartmentContaining(
      String nameKeyword, String departmentKeyword);
}
