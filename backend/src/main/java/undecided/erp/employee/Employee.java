package undecided.erp.employee;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
public class Employee {
  @Id private UUID employeeId;

  @Column(nullable = false)
  private String lastNane;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private LocalDateTime validFrom;

  private LocalDateTime validTo;
}
