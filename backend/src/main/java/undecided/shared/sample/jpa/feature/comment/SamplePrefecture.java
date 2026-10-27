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
 * <p>{@code @Table.comment} と {@code @Column.comment} を使用して、データベースのテーブルとカラムに
 * 「和名;情報種類」の書式でコメントを付与します。DDL生成時にコメントが反映され、データベースツールで確認できます。
 *
 * <p>情報種類は以下の4種類から選択します：
 * <ul>
 *   <li>リソース: 独立したエンティティ（パーティ、個人、組織など）</li>
 *   <li>対応表: 2つ以上のリソース間の関係性（ロール割り当て、住所など）</li>
 *   <li>対照表: コードと名称の対応（都道府県、金融機関、祝日など）</li>
 *   <li>イベント: システム内で発生する出来事（登録完了、更新完了など）</li>
 * </ul>
 */
@Entity
@Table(schema = "undecided_ef", name = "sample_prefectures", comment = "都道府県;対照表")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SamplePrefecture {

  @Id
  @Column(name = "prefecture_id", comment = "都道府県ID;リソース", nullable = false)
  private Long id;

  @Column(name = "prefecture_code", comment = "都道府県コード;対照表", nullable = false, length = 2)
  private String prefectureCode;

  @Column(name = "prefecture_name", comment = "都道府県名;対照表", nullable = false, length = 20)
  private String prefectureName;

  @Column(name = "region", comment = "地方区分;対照表", length = 10)
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
