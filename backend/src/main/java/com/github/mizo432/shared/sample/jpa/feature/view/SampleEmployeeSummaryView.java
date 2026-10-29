package com.github.mizo432.shared.sample.jpa.feature.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * パーティサマリーViewのサンプルエンティティです。
 *
 * <p>このエンティティは、データベースのView（v_party_summary）をJPAエンティティとして
 * マッピングするパターンを示します。読み取り専用の集計データに有効です。
 *
 * <p>Viewのコメントは「和名;情報種類」の書式で設定します。
 *
 * <p>主な特徴:
 * <ul>
 *   <li>{@code @Table(name = "v_party_summary", comment = "パーティサマリー;対応表")} でView名とコメントを指定</li>
 *   <li>{@code @Immutable} で読み取り専用としてマーク（INSERT/UPDATE/DELETEが禁止される）</li>
 *   <li>通常のエンティティと同様にリポジトリで使用可能</li>
 * </ul>
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/276">Issue #276</a>
 */
@Entity
@Table(name = "v_party_summary", schema = "undecided_ef", comment = "パーティサマリー;対応表")
@org.hibernate.annotations.Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleEmployeeSummaryView {

  @Id
  @Column(name = "party_id", comment = "パーティID;リソース", nullable = false)
  private Long partyId;

  @Column(name = "party_type", comment = "パーティタイプ;対応表", nullable = false, length = 10)
  private String partyType;

  @Column(name = "simple_mame", comment = "パーティ名称;対応表", nullable = false, length = 50)
  private String simpleMame;

  /**
   * パーティサマリーViewを作成します。
   *
   * @param partyId パーティの一意な識別子
   * @param partyType パーティタイプ（PERSON, COMPANY など）
   * @param simpleMame パーティの名称
   */
  public SampleEmployeeSummaryView(Long partyId, String partyType, String simpleMame) {
    this.partyId = partyId;
    this.partyType = partyType;
    this.simpleMame = simpleMame;
  }
}
