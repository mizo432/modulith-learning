package undecided.generic.addressReg.internal;

import org.jspecify.annotations.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import undecided.generic.addressReg.spi.Prefecture;

@Repository
public interface PrefectureRepository extends CrudRepository<Prefecture, Long> {

  Prefecture findByPrefectureCode(@NonNull String prefectureCode);
}
