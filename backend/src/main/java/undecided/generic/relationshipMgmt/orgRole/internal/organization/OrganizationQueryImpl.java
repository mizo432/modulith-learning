package undecided.generic.relationshipMgmt.orgRole.internal.organization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.generic.relationshipMgmt.orgRole.spi.Organization;
import undecided.generic.relationshipMgmt.orgRole.spi.OrganizationQuery;
import undecided.supporting.primitiveOld.Lists2;

@Service
@RequiredArgsConstructor
public class OrganizationQueryImpl implements OrganizationQuery {
  private final OrganizationRepository organizationRepository;

  @Override
  public List<Organization> findAll() {
    return Lists2.newArrayList(organizationRepository.findAll());
  }

  @Override
  public Optional<Organization> findById(UUID id) {
    return organizationRepository.findById(id);
  }
}
