package com.sldeditor.test.unit.common.property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.common.property.EncryptedPropertiesFactory;
import org.junit.jupiter.api.Test;

/**
 * Unit test for EncryptedPropertiesFactory.
 *
 * <p>{@link com.sldeditor.common.property.EncryptedPropertiesFactory}
 *
 * @author Robert Ward (SCISYS)
 */
public class EncryptedPropertiesFactoryTest {

    @Test
    public void testEncryptedProperties() {
        // Try apache.commons
        String expectedString = "test string 1234";
        String enrypted = EncryptedPropertiesFactory.getInstance().encrypt(expectedString);

        enrypted = EncryptedPropertiesFactory.getInstance().encrypt(expectedString);
        String actualApacheCommonsResult =
                EncryptedPropertiesFactory.getInstance().decrypt(enrypted);
        assertEquals(expectedString, actualApacheCommonsResult);
    }
}
