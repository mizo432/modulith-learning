package com.github.mizo432.shared.sample.jpa.feature.search;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 検索パターンのサンプルリポジトリです。
 *
 * <p>メソッド名派生クエリ、JPQL LIKE、Specificationなどの検索アプローチを示します。
 */
@Repository
public interface SampleProductSearchRepository
    extends JpaRepository<SampleProductSearch, SnowflakeId>,
        JpaSpecificationExecutor<SampleProductSearch> {

  /**
   * 名前が指定した文字列で始まる製品を検索します（先頭一致）。
   *
   * @param name 先頭文字列
   * @return 一致する製品リスト
   */
  List<SampleProductSearch> findByNameStartingWith(String name);

  /**
   * 名前に指定した文字列を含む製品を検索します（部分一致）。
   *
   * @param keyword 検索キーワード
   * @return 一致する製品リスト
   */
  List<SampleProductSearch> findByNameContaining(String keyword);

  /**
   * 名前が指定した文字列で終わる製品を検索します（末尾一致）。
   *
   * @param suffix 末尾文字列
   * @return 一致する製品リスト
   */
  List<SampleProductSearch> findByNameEndingWith(String suffix);

  /**
   * カテゴリで製品を検索します。
   *
   * @param category カテゴリ名
   * @return 一致する製品リスト
   */
  List<SampleProductSearch> findByCategory(String category);

  /**
   * 名前とカテゴリの両方で部分一致検索します。
   *
   * @param nameKeyword 名前キーワード
   * @param categoryKeyword カテゴリキーワード
   * @return 一致する製品リスト
   */
  List<SampleProductSearch> findByNameContainingAndCategoryContaining(
      String nameKeyword, String categoryKeyword);

  /**
   * JPQL LIKEを使用して大文字小文字を無視して部分一致検索します。
   *
   * @param keyword 検索キーワード
   * @return 一致する製品リスト
   */
  @Query(
      "SELECT p FROM SampleProductSearch p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
  List<SampleProductSearch> findByNameContainingIgnoreCase(@Param("keyword") String keyword);
}
