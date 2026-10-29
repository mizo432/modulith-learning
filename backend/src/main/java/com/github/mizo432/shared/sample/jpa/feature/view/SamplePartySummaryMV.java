package com.github.mizo432.shared.sample.jpa.feature.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * パーティサマリーMaterialized Viewのサンプルエンティティです。
 *
 * <p>このエンティティは、データベースのMaterialized View（mv_party_summary）をJPAエンティティとして
 * マッピングするパターンを示します。集計結果を物理的に保存し、高速な読み取りを提供します。
 *
 * <p>Materialized Viewのコメントは「和名;情報種類」の書式で設定します。
 *
 * <p>主な特徴:
 * <ul>
 *   <li>{@code @Table(name = "mv_party_summary", comment = "パーティサマリー;対応表")} でMaterialized View名とコメントを指定</li>
 *   <li>{@code @Immutable} で読み取り専用としてマーク（INSERT/UPDATE/DELETEが禁止される）</li>
 *   <li>Materialized Viewは{@code REFRESH MATERIALIZED VIEW}でデータを更新</li>
 *   <li>通常のViewと違い、インデックスを定義可能</li>
 * </ul>
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/276">Issue #276</a>
 */
@Entity
@Table(name = "mv_party_summary", schema = "undecided_ef", comment = "パーティサマリー;対応表")
@org.hibernate.annotations.Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SamplePartySummaryMV {

  @Id
  @Column(name = "party_type", comment = "パーティタイプ;対応表", nullable = false, length = 20)
  private String partyType;

  @Column(name = "party_count", comment = "パーティ数;対応表", nullable = false)
  private Long partyCount;

  /**
   * パーティサマリーMaterialized Viewを作成します。
   *
   * @param partyType パーティタイプ（PERSON, COMPANY など）
   * @param partyCount パーティ数
   */
  public SamplePartySummaryMV(String partyType, Long partyCount) {
    this.partyType = partyType;
    this.partyCount = partyCount;
  }
}
