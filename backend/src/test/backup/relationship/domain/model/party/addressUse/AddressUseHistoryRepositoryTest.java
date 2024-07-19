package undecided.erp.relationship.domain.model.party.addressUse;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import undecided.erp.relationship.domain.model.party.address.Address;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.date.ApplicableDate;
import undecided.erp.shared.date.DateInterval;
import undecided.erp.shared.entity.SnowflakeId;

@DataJpaTest
//@Transactional
@Tag("medium")
//@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=none"})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressUseHistoryRepositoryTest {

  private static final Logger log = LoggerFactory.getLogger(AddressUse.class);

  @Autowired
  private AddressUseHistoryRepository addressUseHistoryRepository;

  @Nested
  @Disabled
  @Tag("medium")
  class FindByPartyIdAndTargetDateTest {

    @Test
    void shouldFindAddressUseByPartyIdAndTargetDate() {
      SnowflakeId<AddressUse> id = SnowflakeId.newInstance();
      SnowflakeId<Party> partyId = SnowflakeId.newInstance();
      SnowflakeId<Address> addressId = SnowflakeId.newInstance();
      DateInterval interval = DateInterval.create(ApplicableDate.now(), ApplicableDate.MAX);
      log.info("interval: " + interval);
      AddressUseType type = AddressUseType.NORMAL;
      ReasonForTransferType reasonForTransfer = ReasonForTransferType.INITIAL;

      AddressUse actualAddressUse = AddressUse.create(
          id,
          partyId,
          addressId,
          interval,
          type,
          reasonForTransfer);

      log.debug("actualAddressUse: " + actualAddressUse);

      addressUseHistoryRepository.save(actualAddressUse);

      ApplicableDate targetDate = ApplicableDate.now();

      Optional<AddressUse> result = addressUseHistoryRepository.findByPartyIdAndTargetDate(partyId,
          targetDate);

      assertThat(result)
          .isPresent();
      assertThat(result.get().getPartyId())
          .isEqualTo(partyId);

    }

    @Test
    void shouldNotFindAddressUseByPartyIdAndTargetDate() {

      SnowflakeId<Party> partyId = SnowflakeId.of(200L);
      ApplicableDate targetDate = ApplicableDate.now();

      Optional<AddressUse> result = addressUseHistoryRepository.findByPartyIdAndTargetDate(partyId,
          targetDate);

      assertThat(result)
          .isEmpty();

    }

  }
}