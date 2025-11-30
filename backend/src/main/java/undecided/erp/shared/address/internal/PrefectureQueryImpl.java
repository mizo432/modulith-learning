package undecided.erp.shared.address.internal;

import static undecided.erp.common.precondition.ObjectPrecondition.checkNotNull;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import undecided.erp.shared.address.spi.Prefecture;
import undecided.erp.shared.address.spi.PrefectureQuery;

@Service
@RequiredArgsConstructor
public class PrefectureQueryImpl implements PrefectureQuery {
  private final PrefectureRepository repository;

  @Override
  public @NonNull Optional<Prefecture> findByCode(@NonNull String code) {
    checkNotNull(code, () -> new NullPointerException("code must not null"));
    return Optional.ofNullable(repository.findByCode(code));
  }
}
