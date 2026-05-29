package undecided.erp.department;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import undecided.erp.common.entity.SnowflakeId;
import undecided.erp.department.internal.DepartmentCode;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @Id
    private SnowflakeId id = SnowflakeId.EMPTY;
    @Column(unique = true, nullable = false, length = 13, name = "code")
    private DepartmentCode code = DepartmentCode.EMPTY;

    public Department update(@NonNull Department department) {
        return this;
    }

}
