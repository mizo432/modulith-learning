package undecided.supporting.rerlationshipMgmt.organization.internal;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.generic.primitiveOld.primitiveOld.Lists2;
import undecided.supporting.rerlationshipMgmt.organization.spi.Organization;
import undecided.supporting.rerlationshipMgmt.organization.spi.OrganizationQuery;

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
