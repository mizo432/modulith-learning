package undecided.shared.sample.jpa.feature.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * テーブルコメントとカラムコメントの使用パターンを示すサンプルエンティティです。
 *
 * <p>{@code @Table.comment} でテーブルの目的を、{@code @Column.comment} で各カラムの用途を
 * 自己文書化します。DDL生成時にコメントが反映され、データベースツールで確認できます。
 */
@Entity
@Table(schema = "undecided_ef", name = "sample_prefectures", comment = "都道府県マスターテーブル")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SamplePrefecture {

  @Id
  @Column(name = "prefecture_id", comment = "都道府県固有識別子", nullable = false)
  private Long id;

  @Column(name = "prefecture_code", comment = "都道府県コード（2桁）", nullable = false, length = 2)
  private String prefectureCode;

  @Column(name = "prefecture_name", comment = "都道府県名", nullable = false, length = 20)
  private String prefectureName;

  @Column(name = "region", comment = "地方区分（関東、関西など）", length = 10)
  private String region;

  public SamplePrefecture(Long id, String prefectureCode, String prefectureName) {
    this.id = id;
    this.prefectureCode = prefectureCode;
    this.prefectureName = prefectureName;
  }

  public SamplePrefecture(Long id, String prefectureCode, String prefectureName, String region) {
    this.id = id;
    this.prefectureCode = prefectureCode;
    this.prefectureName = prefectureName;
    this.region = region;
  }
}
