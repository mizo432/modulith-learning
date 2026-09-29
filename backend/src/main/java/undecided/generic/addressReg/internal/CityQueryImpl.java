package undecided.generic.addressReg.internal;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.generic.addressReg.spi.City;
import undecided.generic.addressReg.spi.CityQuery;

/** {@link CityQuery} の実装クラス。 */
@Service
@RequiredArgsConstructor
public class CityQueryImpl implements CityQuery {

  private final CityRepository repository;

  @Override
  public @NonNull Optional<City> findByLgCode(@NonNull String lgCode) {
    checkNotNull(lgCode, () -> new IllegalArgumentException("lgCode must not be null"));
    return Optional.ofNullable(repository.findByLgCode(lgCode));
  }

  @Override
  public @NonNull List<City> findByPrefectureId(@NonNull Long prefectureId) {
    checkNotNull(prefectureId, () -> new IllegalArgumentException("prefectureId must not be null"));
    return repository.findByPrefectureId(prefectureId);
  }
}
