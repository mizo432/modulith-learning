package undecided.generic.bankReg.internal;

import static undecided.supporting.precondition.ObjectPrecondition.checkNotNull;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import undecided.generic.bankReg.spi.Bank;
import undecided.generic.bankReg.spi.BankQuery;

/** {@link BankQuery} の実装クラス。 */
@Service
@RequiredArgsConstructor
public class BankQueryImpl implements BankQuery {

  private final BankRepository bankRepository;

  @Override
  public @NonNull Optional<Bank> findByBankCode(@NonNull String bankCode) {
    checkNotNull(bankCode, () -> new IllegalArgumentException("bankCode must not be null"));
    return bankRepository.findById(bankCode);
  }

  @Override
  public @NonNull List<Bank> findAll() {
    return bankRepository.findAllByOrderByBankCodeAsc();
  }
}
