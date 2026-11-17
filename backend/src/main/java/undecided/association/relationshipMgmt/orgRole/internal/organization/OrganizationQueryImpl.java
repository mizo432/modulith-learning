package undecided.association.relationshipMgmt.orgRole.internal.organization;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.association.relationshipMgmt.orgRole.spi.Organization;
import undecided.association.relationshipMgmt.orgRole.spi.OrganizationQuery;
import undecided.supporting.snowflake.SnowflakeId;
import com.github.mizo432.shared.common.primitive.Lists2;

@Service
@RequiredArgsConstructor
public class OrganizationQueryImpl implements OrganizationQuery {
  private final OrganizationRepository organizationRepository;

  @Override
  public List<Organization> findAll() {
    return Lists2.newArrayList(organizationRepository.findAll());
  }

  @Override
  public Optional<Organization> findById(SnowflakeId id) {
    return organizationRepository.findById(id);
  }
}
