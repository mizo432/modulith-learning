package undecided.agiloom.productDiscovery.internal.presentation;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
  private String name;
  private String description;
  private UUID managerId;
}
