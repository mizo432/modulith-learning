package com.github.mizo432.shared.sample.jpa.feature.dbobject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * ストアドファンクションのサンプルリポジトリです。
 *
 * <p>このクラスは、JPAのネイティブクエリ機能を使用して
 * データベースのストアドファンクションを操作するパターンを示します。
 *
 * <p>主な特徴:
 * <ul>
 *   <li>{@code EntityManager#createNativeQuery()} でファンクションを呼び出し</li>
 *   <li>パラメータをバインドして動的なクエリを実行</li>
 *   <li>読み取り専用トランザクションで安全に実行</li>
 * </ul>
 *
 * <p>使用例:
 * <pre>{@code
 * @PersistenceContext
 * private EntityManager em;
 *
 * public Long countPartiesByType(String partyType) {
 *   Query query = em.createNativeQuery(
 *       "SELECT func_count_parties_by_type(:partyType)"
 *   );
 *   query.setParameter("partyType", partyType);
 *   return (Long) query.getSingleResult();
 * }
 * }</pre>
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/277">Issue #277</a>
 */
@Repository
@Transactional(readOnly = true)
public class SampleFunction {

  @PersistenceContext private EntityManager entityManager;

  /**
   * パーティタイプごとのパーティ数を取得します。
   *
   * <p>ストアドファンクション（func_count_parties_by_type）を呼び出して、
   * 指定されたパーティタイプを持つパーティの数を返します。
   *
   * @param partyType パーティタイプ（例: "PERSON", "COMPANY"）
   * @return 指定されたパーティタイプのパーティ数
   */
  public Long countPartiesByType(String partyType) {
    Query query =
        entityManager.createNativeQuery(
            "SELECT undecided_as.func_count_parties_by_type(:partyType)");
    query.setParameter("partyType", partyType);
    return ((Number) query.getSingleResult()).longValue();
  }

  /**
   * 全パーティタイプごとのパーティ数を取得します。
   *
   * <p>partiesテーブルからパーティタイプごとの集計を取得します。
   *
   * @return パーティタイプごとのカウントマップ
   */
  @SuppressWarnings("unchecked")
  public java.util.Map<String, Long> countAllPartiesByType() {
    Query query =
        entityManager.createNativeQuery(
            "SELECT party_type, COUNT(*) FROM undecided_as.parties GROUP BY party_type");
    java.util.List<Object[]> results = query.getResultList();
    java.util.Map<String, Long> result = new java.util.HashMap<>();
    for (Object[] row : results) {
      result.put((String) row[0], ((Number) row[1]).longValue());
    }
    return result;
  }
}
