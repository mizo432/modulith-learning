package undecided.shared.sample.jpa.entity.relationship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import undecided.supporting.snowflake.SnowflakeId;

/**
 * 従業員プロフィール（Employee Profile）を表すサンプルエンティティです。
 *
 * <p>{@code @OneToOne}関連付けのサンプルとして、 一人の従業員（SampleEmployee）が一つのプロフィールを持つ関係を示します。
 *
 * <p>特徴:
 *
 * <ul>
 *   <li>{@code @OneToOne}で一対一の関連付けを定義
 *   <li>{@code @JoinColumn}で外部キーカラムを指定
 *   <li>{@code optional = true}でオプションな関係を許容
 * </ul>
 */
@Entity
@Table(name = "sample_employee_profiles")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleEmployeeProfile {

  @Id private SnowflakeId profileId;

  /** このプロフィールに関連付けられた従業員です。 {@code @OneToOne}により、一つのプロフィールは一人の従業員に関連付けられます。 */
  @OneToOne(optional = false)
  @JoinColumn(name = "employee_id", nullable = false, unique = true)
  private SampleEmployee employee;

  /** 住所 */
  @Column(length = 255)
  private String address;

  /** 電話番号 */
  @Column(length = 20)
  private String phoneNumber;

  /** メールアドレス */
  @Column(length = 100)
  private String email;

  public SampleEmployeeProfile(
      SnowflakeId profileId,
      SampleEmployee employee,
      String address,
      String phoneNumber,
      String email) {
    this.profileId = profileId;
    this.employee = employee;
    this.address = address;
    this.phoneNumber = phoneNumber;
    this.email = email;
  }
}
