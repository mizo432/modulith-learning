package undecided.association.relationshipMgmt.personRole.spi.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import com.github.mizo432.shared.converter.EncryptedStringConverter;

@Entity
@Table(name = "employees")
@Getter
@Setter
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
}
