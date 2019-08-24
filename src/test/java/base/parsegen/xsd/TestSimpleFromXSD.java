package base.parsegen.xsd;

import base.v3.AbstractApplicationBuilder;
import fullsuite.XMLGenTest;

import static junit.framework.TestCase.fail;

public class TestSimpleFromXSD extends XMLGenTest {

    final boolean preserveConstraints = false;

    @Override
    public String getOrg() {
        return "shipping";
    }

    @Override
    public String getYAMLSource() {
        return "base/parsegen/xsd/shipping-simple/shipping.yml";
    }

    @Override
    public String getBaselineDir() {
        return "expected_out/hospital_log";
    }

    @Override
    protected String getExportDir() {
        return "/tmp/test_shipping_app";
    }

    @Override
    protected void applyOverrides(final AbstractApplicationBuilder abstractApplicationBuilder) {
        fail();
    }

}
