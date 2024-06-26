package undecided.erp.addressMgmt.model.machiAza;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import undecided.erp.addressMgmt.domain.model.machiAza.MachiAzaEffectiveDate;
import undecided.erp.shared.date.ApplicableDate;

/**
 * Tests for the MachiAzaEffectiveDate class. Specifically, this focuses on the getValue method.
 */
public class MachiAzaEffectiveDateTest {

  @Test
  void testGetValueWithNullEffectiveDate() {
    MachiAzaEffectiveDate effectiveDate = MachiAzaEffectiveDate.of(null);
    assertSame(MachiAzaEffectiveDate.EMPTY, effectiveDate);
  }

  @Test
  void testGetValueWithNonNullEffectiveDate() {
    LocalDate localDate = LocalDate.now();
    ApplicableDate applicableDate = ApplicableDate.of(localDate);
    MachiAzaEffectiveDate effectiveDate = MachiAzaEffectiveDate.of(applicableDate);
    assertEquals(localDate, effectiveDate.getValue());
  }

}