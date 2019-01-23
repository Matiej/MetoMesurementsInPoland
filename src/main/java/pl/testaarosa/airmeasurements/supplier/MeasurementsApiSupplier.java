package pl.testaarosa.airmeasurements.supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.testaarosa.airmeasurements.cfg.MeasurementsApiSupplierConfig;

import java.net.URI;

@Service
public class MeasurementsApiSupplier {

    private final MeasurementsApiSupplierConfig msConfig;

    @Autowired
    public MeasurementsApiSupplier(MeasurementsApiSupplierConfig msConfig) {
        this.msConfig = msConfig;
    }

    public URI giosApiSupplierAll() {
        return UriComponentsBuilder.fromHttpUrl(msConfig.getGiosAll())
                .build()
                .encode()
                .toUri();
    }

    public URI giosApiSupplierIndex(Integer stationId) {
        return UriComponentsBuilder.fromHttpUrl(msConfig.getGiosIndex())
                .path(stationId.toString())
                .build()
                .encode()
                .toUri();
    }

    public URI imgwApiSupplierAll() {
        return UriComponentsBuilder.fromHttpUrl(msConfig.getImgwAll())
                .build()
                .encode()
                .toUri();
    }
}
