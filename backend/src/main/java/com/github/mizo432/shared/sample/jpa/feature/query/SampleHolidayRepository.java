package com.github.mizo432.shared.sample.jpa.feature.query;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Query/@Modifyingの使用パターンを示すサンプルリポジトリです。
 *
 * <p>標準のリポジトリメソッドでは表現できないクエリを、 @Queryアノテーションを使用してJPQLで定義します。
 */
@Repository
public interface SampleHolidayRepository extends JpaRepository<SampleHoliday, Long> {

  /**
   * 日付範囲で祝日を検索するカスタムJPQLクエリです。
   *
   * @param from 開始日
   * @param to 終了日
   * @return 日付範囲内の祝日リスト（昇順）
   */
  @Query(
      "SELECT h FROM SampleHoliday h WHERE h.holidayDate >= :from AND h.holidayDate <= :to ORDER BY h.holidayDate ASC")
  List<SampleHoliday> findByHolidayDateBetween(
      @Param("from") LocalDate from, @Param("to") LocalDate to);

  /**
   * アクティブな祝日のみを検索するカスタムJPQLクエリです。
   *
   * @return アクティブな祝日リスト
   */
  @Query("SELECT h FROM SampleHoliday h WHERE h.active = true ORDER BY h.holidayDate ASC")
  List<SampleHoliday> findActiveHolidays();

  /**
   * 祝日名で部分一致検索するカスタムJPQLクエリです。
   *
   * @param keyword 検索キーワード
   * @return 一致する祝日リスト
   */
  @Query(
      "SELECT h FROM SampleHoliday h WHERE LOWER(h.holidayName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<SampleHoliday> findByHolidayNameContaining(@Param("keyword") String keyword);

  /**
   * 指定されたdatasetId以外の祝日を削除する@Modifyingクエリです。
   *
   * @return 削除された行数
   */
  @Modifying
  @Query("DELETE FROM SampleHoliday h WHERE h.id <> :id")
  int deleteByIdNot(@Param("id") Long id);

  /**
   * アクティブでない祝日を非アクティブにする@Modifyingクエリです。
   *
   * @return 更新された行数
   */
  @Modifying
  @Query("UPDATE SampleHoliday h SET h.active = false WHERE h.holidayDate < :cutoffDate")
  int deactivateBefore(@Param("cutoffDate") LocalDate cutoffDate);

  /**
   * ネイティブクエリを使用して祝日数をカウントします。
   *
   * @return 祝日の総数
   */
  @Query(value = "SELECT COUNT(*) FROM sample_holidays WHERE is_active = true", nativeQuery = true)
  long countActiveHolidays();
}
