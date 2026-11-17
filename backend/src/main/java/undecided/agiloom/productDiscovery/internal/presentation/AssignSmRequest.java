package undecided.agiloom.productDiscovery.internal.presentation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignSmRequest {
  @NotNull private UUID smEmployeeId;
  @NotBlank private String smName;
}
