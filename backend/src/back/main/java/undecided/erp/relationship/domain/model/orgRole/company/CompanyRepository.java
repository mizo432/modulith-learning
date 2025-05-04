package undecided.erp.relationship.domain.model.orgRole.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.shared.entity.SnowflakeId;

@Repository
public interface CompanyRepository extends JpaRepository<Company, SnowflakeId> {

}
