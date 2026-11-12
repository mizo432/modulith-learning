package com.github.mizo432.shared.sample.jpa.feature.dbobject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * データベースシノニムのサンプルエンティティです。
 *
 * <p>このエンティティは、データベースシノニムを介して元のテーブルにアクセスするパターンを示します。
 * シノニムは、オブジェクトの別名を提供し、スキーマ名やオブジェクト名の変更からアプリケーションを保護します。
 *
 * <p>主な特徴:
 * <ul>
 *   <li>{@code @Table(name = "syn_parties")} でシノニム名を指定</li>
 *   <li>シノニムを通じて元のテーブル（parties）にアクセス</li>
 *   <li>読み取り専用として使用することを推奨</li>
 * </ul>
 *
 * <p>シノニムの利点:
 * <ul>
 *   <li>スキーマ名の変更からアプリケーションを保護</li>
 *   <li>リモートデータベースオブジェクトへのアクセスを簡素化</li>
 *   <li>オブジェクト名の変更時の影響を最小限に抑える</li>
 * </ul>
 *
 * @see <a href="https://github.com/mizo432/modulith-learning/issues/277">Issue #277</a>
 */
@Entity
@Table(name = "syn_parties", schema = "undecided_as")
@org.hibernate.annotations.Immutable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleSynonym {

  @Id
  @Column(name = "party_id", nullable = false)
  private Long partyId;

  @Column(name = "party_type", nullable = false, length = 10)
  private String partyType;

  @Column(name = "simple_name", nullable = false, length = 100)
  private String simpleName;

  /**
   * シノニム経由でパーティ情報を取得するためのコンストラクタです。
   *
   * @param partyId パーティの一意な識別子
   * @param partyType パーティタイプ（PERSON, COMPANY など）
   * @param simpleName パーティの名称
   */
  public SampleSynonym(Long partyId, String partyType, String simpleName) {
    this.partyId = partyId;
    this.partyType = partyType;
    this.simpleName = simpleName;
  }
}
