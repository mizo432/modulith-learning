package undecided.erp.addressMgmt.model.city;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CityNamesTest {

    @Test
    void testReconstruct() {
        String testName = "Tokyo";
        String testKana = "Tōkyō";
        String testRoma = "Tokyo";
        
        CityNames names = CityNames.reconstruct(testName, testKana, testRoma);

        assertEquals(testName, names.getName().getValue());
        assertEquals(testKana, names.getKana().getValue());
        assertEquals(testRoma, names.getRoma().getValue());
    }

    @Test
    void testReconstructWithNullValues() {
        String testName = null;
        String testKana = null;
        String testRoma = null;
        
        CityNames names = CityNames.reconstruct(testName, testKana, testRoma);

        assertNull(names.getName().getValue());
        assertNull(names.getKana().getValue());
        assertNull(names.getRoma().getValue());
    }

    @Test
    void testCreate() {
        String testName = "Tokyo";
        String testKana = "Tōkyō";
        String testRoma = "Tokyo";

        CityNames namesCreated = CityNames.create(testName, testKana, testRoma);

        assertEquals(testName, namesCreated.getName().getValue());
        assertEquals(testKana, namesCreated.getKana().getValue());
        assertEquals(testRoma, namesCreated.getRoma().getValue());
    }
}