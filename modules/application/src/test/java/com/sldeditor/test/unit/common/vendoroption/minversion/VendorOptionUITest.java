package com.sldeditor.test.unit.common.vendoroption.minversion;

import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.vendoroption.minversion.VendorOptionUI;
import com.sldeditor.ui.panels.GetMinimumVersionInterface;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for VendorOptionUI.
 *
 * <p>{@link com.sldeditor.common.vendoroption.minversion.VendorOptionUI}
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionUITest {

    @Test
    public void testVendorOptionUI() {
        VendorOptionUI testObj =
                new VendorOptionUI(
                        new GetMinimumVersionInterface() {

                            @Override
                            public void getMinimumVersion(
                                    Object parentObj,
                                    Object sldObj,
                                    List<VendorOptionPresent> vendorOptionsPresentList) {}
                        });

        testObj.createUI();

        testObj.updatedSLD(null, null);
        testObj.vendorOptionsUpdated(null);
        testObj.populate(null);
    }
}
