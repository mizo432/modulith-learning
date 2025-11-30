package undecided.erp.shared.address.internal;

import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.erp.shared.address.spi.Prefecture;

@Repository
public interface PrefectureRepository extends CrudRepository<Prefecture, UUID> {
  Prefecture findByCode(@NonNull String code);
}
