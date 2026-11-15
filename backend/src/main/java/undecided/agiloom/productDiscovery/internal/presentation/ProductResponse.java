package undecided.agiloom.productDiscovery.internal.presentation;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import undecided.agiloom.productDiscovery.spi.ProductStatus;
import undecided.supporting.snowflake.SnowflakeId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
  private SnowflakeId id;
  private String name;
  private String description;
  private UUID managerId;
  private ProductStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
