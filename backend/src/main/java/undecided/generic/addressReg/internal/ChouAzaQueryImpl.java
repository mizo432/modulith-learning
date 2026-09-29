package undecided.generic.addressReg.internal;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.generic.addressReg.spi.ChouAza;
import undecided.generic.addressReg.spi.ChouAzaQuery;

/** {@link ChouAzaQuery} の実装クラス。 */
@Service
@RequiredArgsConstructor
public class ChouAzaQueryImpl implements ChouAzaQuery {

  private final ChouAzaRepository repository;

  @Override
  public @NonNull Optional<ChouAza> findByLgCodeAndMachiazaCode(
      @NonNull String lgCode, @NonNull String machiazaCode) {
    checkNotNull(lgCode, () -> new IllegalArgumentException("lgCode must not be null"));
    checkNotNull(machiazaCode, () -> new IllegalArgumentException("machiazaCode must not be null"));
    return Optional.ofNullable(repository.findByLgCodeAndMachiazaCode(lgCode, machiazaCode));
  }

  @Override
  public @NonNull List<ChouAza> findByCityId(@NonNull Long cityId) {
    checkNotNull(cityId, () -> new IllegalArgumentException("cityId must not be null"));
    return repository.findByCityId(cityId);
  }

  @Override
  public @NonNull List<ChouAza> findByLgCode(@NonNull String lgCode) {
    checkNotNull(lgCode, () -> new IllegalArgumentException("lgCode must not be null"));
    return repository.findByLgCode(lgCode);
  }
}
