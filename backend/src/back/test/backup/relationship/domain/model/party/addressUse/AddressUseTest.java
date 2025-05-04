package undecided.erp.relationship.domain.model.party.addressUse;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import undecided.erp.relationship.domain.model.party.address.Address;
import undecided.erp.relationship.domain.model.party.party.Party;
import undecided.erp.shared.date.ApplicableDate;
import undecided.erp.shared.date.DateInterval;
import undecided.erp.shared.entity.SnowflakeId;

class AddressUseTest {

  @Nested
  class ObTest {

    @Test
    void shouldCreateAddressUseWithGivenParameters() {
      SnowflakeId<AddressUse> id = SnowflakeId.newInstance();
      SnowflakeId<Party> partyId = SnowflakeId.newInstance();
      SnowflakeId<Address> addressId = SnowflakeId.newInstance();
      DateInterval interval = DateInterval.create(ApplicableDate.now(), ApplicableDate.MAX);
      AddressUseType type = AddressUseType.NORMAL;
      ReasonForTransferType reasonForTransfer = ReasonForTransferType.INITIAL;

      AddressUse expectedAddressUse = new AddressUse(id, partyId, addressId, interval, type,
          reasonForTransfer);

      AddressUse actualAddressUse = AddressUse.create(id, partyId, addressId, interval, type,
          reasonForTransfer);

      assertThat(actualAddressUse).isEqualTo(expectedAddressUse);
    }

  }


  @Nested
  class JsonProcessingTests {

    @Test
    void shouldCorrectlyProcessAndMatchApplicableDateInJson() throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

      AddressUse sourceAddressUse = AddressUse.create(
          SnowflakeId.newInstance(),
          SnowflakeId.newInstance(),
          SnowflakeId.newInstance(),
          DateInterval.create(ApplicableDate.now(), ApplicableDate.MAX),
          AddressUseType.NORMAL,
          ReasonForTransferType.INITIAL
      );

      String json = objectMapper.writeValueAsString(sourceAddressUse);
      System.out.println(json);
      //     log.debug(json);
      AddressUse actualAddressUse = objectMapper.readValue(json, AddressUse.class);
      assertThat(actualAddressUse)
          .isEqualTo(sourceAddressUse);


    }
  }

}