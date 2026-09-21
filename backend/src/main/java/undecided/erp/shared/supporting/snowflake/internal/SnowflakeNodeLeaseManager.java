package undecided.erp.shared.supporting.snowflake.internal;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnowflakeNodeLeaseManager {

  private final SnowflakeNodeAllocator allocator;

  @Value("${spring.application.name}")
  private String applicationName;
  private SnowflakeNode lease;

  @PostConstruct
  public void start() {
    lease = allocator.allocate(applicationName);
    log.info("Allocated snowflake node with id {}", lease.getNodeId());
  }

  public NodeId nodeId() {
    SnowflakeNode current = lease;
    if (current == null) {
      throw new IllegalStateException("Snowflake node is not initialized");
    }
    var currentNodeId = (int) current.getNodeId();
    return new NodeId(currentNodeId);
  }

  public boolean renew() {
    SnowflakeNode current = lease;
    if (current == null) {
      throw new IllegalStateException("Snowflake node is not initialized");
    }
    return allocator.renew(current);
  }

}
