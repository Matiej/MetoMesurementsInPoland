package pl.testaarosa.airmeasurements.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MeasurementsApiSupplierConfig {

    @Value("${gios.api.endpoint.all}")
    private String giosAll;

    @Value("${gios.api.endpoint.index}")
    private String giosIndex;

    @Value("${imgw.api.endpoint.all}")
    private String imgwAll;

    public String getGiosAll() {
        return giosAll;
    }

    public String getGiosIndex() {
        return giosIndex;
    }

    public String getImgwAll() {
        return imgwAll;
    }
}
