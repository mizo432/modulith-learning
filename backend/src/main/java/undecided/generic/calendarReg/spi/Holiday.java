package undecided.generic.calendarReg.spi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 祝日情報を管理するエンティティクラス。
 *
 * <p>このクラスは、データベースの「calendar_reg.holiday」テーブルに対応するものであり、 祝日・休日に関する情報を保持します。
 * 各フィールドは、祝日ID、祝日月日、祝日名称、備考などを表します。
 *
 * <p>エンティティとして、JPAやHibernateを利用してデータベースと連携するための定義が施されています。
 */
@Getter
@Setter
@ToString
@Entity
@Table(schema = "calendar_reg", name = "holiday", comment = "holiday table")
public class Holiday {

  /**
   * 祝日を一意に識別するためのIDを格納するフィールド。
   *
   * <p>データベース上では "holiday_id" カラムに対応しており、null 不可として定義されています。 主に祝日レコードのプライマリキーとして使用されます（例:
   * 20240101）。
   */
  @Id
  @Column(name = "holiday_id", comment = "Unique identifier for the holiday", nullable = false)
  private Long id;

  /**
   * 祝日・休日月日を表すフィールド。
   *
   * <p>データベース上では "holiday_date" カラムに対応しており、null 不可として定義されています。
   */
  @NotNull
  @Column(name = "holiday_date", comment = "Holiday date", nullable = false)
  private LocalDate holidayDate;

  /**
   * 祝日・休日名称を表すフィールド。
   *
   * <p>このフィールドは空であってはならず、最大50文字までの入力が可能です。 データベース上では "holiday_name" カラムに対応し、null 不可として定義されています。
   */
  @Size(max = 50)
  @NotNull
  @Column(name = "holiday_name", comment = "Holiday name", nullable = false, length = 50)
  private String holidayName;

  /**
   * 備考を表すフィールド。
   *
   * <p>このフィールドには、関連する補足情報や自由形式のコメントを保存できます。最大256文字まで格納可能です。
   */
  @Size(max = 256)
  @Column(name = "remarks", comment = "Remarks", length = 256)
  private String remarks;
}
