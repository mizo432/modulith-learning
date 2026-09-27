package undecided.generic.addressReg.internal;

import java.util.UUID;
import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.addressReg.spi.Prefecture;

@Repository
public interface PrefectureRepository extends CrudRepository<Prefecture, UUID> {

  Prefecture findByPrefectureCode(@NonNull String prefectureCode);
}
