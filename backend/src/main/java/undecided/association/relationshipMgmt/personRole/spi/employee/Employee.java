package undecided.association.relationshipMgmt.personRole.spi.employee;

import com.github.mizo432.shared.exception.BusinessException;
import static com.github.mizo432.shared.common.precondition.ObjectPrecondition.checkNotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import com.github.mizo432.shared.converter.EncryptedStringConverter;

@Entity(name = "TestEmployee")
@Table(name = "employees")
@Getter
public class Employee {
  @Id private UUID employeeId;

  @Column(nullable = false)
  @Convert(converter = EncryptedStringConverter.class)
  private String lastName;

  @Column(nullable = false)
  @Convert(converter = EncryptedStringConverter.class)
  private String firstName;

  @Column(nullable = false)
  private LocalDateTime validFrom;

  private LocalDateTime validTo;

  // JPA用パッケージプライベートセッター
  void setEmployeeId(UUID employeeId) {
    this.employeeId = employeeId;
  }

  void setLastName(String lastName) {
    this.lastName = lastName;
  }

  void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  void setValidFrom(LocalDateTime validFrom) {
    this.validFrom = validFrom;
  }

  void setValidTo(LocalDateTime validTo) {
    this.validTo = validTo;
  }

  /**
   * 従業員情報を更新します。
   *
   * @param lastName 姓（null不可）
   * @param firstName 名（null不可）
   * @param validFrom 有効開始日（null不可）
   * @param validTo 有効終了日（null可）
   */
  public void update(@NonNull String lastName, @NonNull String firstName, @NonNull LocalDateTime validFrom, @Nullable LocalDateTime validTo) {
    checkNotNull(lastName, () -> new BusinessException("lastName must not be null"));
    checkNotNull(firstName, () -> new BusinessException("firstName must not be null"));
    checkNotNull(validFrom, () -> new BusinessException("validFrom must not be null"));
    if (lastName.trim().isEmpty()) {
      throw new BusinessException("lastName must not be empty");
    }
    if (firstName.trim().isEmpty()) {
      throw new BusinessException("firstName must not be empty");
    }
    this.lastName = lastName;
    this.firstName = firstName;
    this.validFrom = validFrom;
    this.validTo = validTo;
  }
}
