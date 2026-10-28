package com.github.mizo432.shared.sample.jpa.feature.dbobject;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * ストアドプロシージャのサンプルリポジトリです。
 *
 * <p>このクラスは、ネイティブクエリを使用してデータベースのストアドプロシージャを操作するパターンを示します。
 *
 * <p>主な特徴:
 * <ul>
 *   <li>{@code CALL} 文でストアドプロシージャを呼び出し</li>
 *   <li>ネイティブクエリで結果を取得</li>
 *   <li>トランザクション内で安全に実行</li>
 * </ul>
 *
 * <p>使用例:
 * <pre>{@code
 * @PersistenceContext
 * private EntityManager em;
 *
 * public Long getNextOrderId() {
 *   Query query = em.createNativeQuery("SELECT undecided_as.proc_get_next_order_id()");
 *   return (Long) query.getSingleResult();
 * }
 * }</pre>
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/277">Issue #277</a>
 */
@Repository
@Transactional(readOnly = true)
public class SampleProcedure {

  @PersistenceContext private EntityManager entityManager;

  /**
   * サンプル注文番号シーケンスから次の値を取得します。
   *
   * <p>シーケンス（seq_sample_order）から次の値を取得します。
   *
   * @return 次の注文番号
   */
  @Transactional
  public Long getNextOrderId() {
    Query query = entityManager.createNativeQuery("SELECT nextval('undecided_as.seq_sample_order')");
    return ((Number) query.getSingleResult()).longValue();
  }

  /**
   * サンプルパーティシーケンスから次の値を取得します。
   *
   * @return 次のパーティ番号
   */
  @Transactional
  public Long getNextPartyId() {
    Query query = entityManager.createNativeQuery("SELECT nextval('undecided_as.seq_sample_party')");
    return ((Number) query.getSingleResult()).longValue();
  }
}
