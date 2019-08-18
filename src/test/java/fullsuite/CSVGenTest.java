package fullsuite;

import base.v3.AbstractApplicationBuilder;
import base.v3.CSVApplicationBuilder;

import java.io.IOException;

public abstract class CSVGenTest extends XMLGenTest {

    @Override
    protected AbstractApplicationBuilder getApplicationBuilder() throws IOException {
        return new CSVApplicationBuilder(getOrg(), getSourceFiles(), getExportDir());
    }

}

