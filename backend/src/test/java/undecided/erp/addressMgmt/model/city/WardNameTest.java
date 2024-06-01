package undecided.erp.addressMgmt.model.city;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WardNameTest {

    @Test
    void shouldReconstructNonNullValue() {
        String inputValue = "Ward 123";
        WardName reconstructedWard = WardName.reconstruct(inputValue);
        assertEquals(inputValue, reconstructedWard.getValue());
    }

    @Test
    void shouldReconstructNullValueAsNullObject() {
        String inputValue = null;
        WardName reconstructedWard = WardName.reconstruct(inputValue);
        assertNull(reconstructedWard.getValue());
    }
}