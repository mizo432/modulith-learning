package undecided.erp.relationship.domain.model.party;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRepository extends PagingAndSortingRepository<Party, Long> {

  List<Party> findBySearchNameBefore(String searchName);


}
