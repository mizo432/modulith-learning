package undecided.erp.addressMgmt.model.city;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountyNamesTest {
    
    @Test
    void testReconstruct_withValidParameters_shouldReturnNewCountyNames() {
        String expectedName = "Tokyo";
        String expectedKana = "Toukyou";
        String expectedRoma = "Tokyo";

        CountyNames countyNames = CountyNames.reconstruct(expectedName, expectedKana, expectedRoma);

        assertEquals(expectedName, countyNames.getName().getValue());
        assertEquals(expectedKana, countyNames.getKana().getValue());
        assertEquals(expectedRoma, countyNames.getRoma().getValue());
    }
    
    @Test
    void testCreate_withNonNullName_shouldReturnNewCountyNames() {
        String expectedName = "Osaka";
        String kana = "Oosaka";
        String roma = "Osaka";

        CountyNames countyNames = CountyNames.create(expectedName, kana, roma);

        assertEquals(expectedName, countyNames.getName().getValue());
        assertEquals(kana, countyNames.getKana().getValue());
        assertEquals(roma, countyNames.getRoma().getValue());
    }

    @Test
    void testToString_withValidCountyNames_shouldReturnFormattedString() {
        String name = "Hokkaido";
        String kana = "Hokkaidou";
        String roma = "Hokkaido";

        CountyNames countyNames = new CountyNames(CountryName.of(name),
        CountryKana.of(kana),
        CountryRoma.of(roma));
        
        String expectedString = "CountyNames{name=" + name + ", kana=" + kana + ", roma=" + roma + "}";
        assertEquals(expectedString, countyNames.toString());
    }    
}