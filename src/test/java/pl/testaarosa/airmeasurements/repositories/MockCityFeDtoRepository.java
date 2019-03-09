package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.model.CityFeDto;

import java.util.Arrays;
import java.util.List;

public class MockCityFeDtoRepository {

    private final MockAirMeasurementRepository airMeasurementRepository = new MockAirMeasurementRepository();
    private final MockSynopticMeasurementRepository mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();

    public List<CityFeDto> cityFeDtoList() {
        CityFeDto cityFeDto1 = new CityFeDto();
        cityFeDto1.setName("Warszawa");
        cityFeDto1.setSynopticMeasurement(mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest().get(0));
        cityFeDto1.setAirMeasurementList(airMeasurementRepository.airMeasurements1());

        CityFeDto cityFeDto2 = new CityFeDto();
        cityFeDto2.setName("Poznan");
        cityFeDto2.setSynopticMeasurement(mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest().get(2));
        cityFeDto2.setAirMeasurementList(airMeasurementRepository.airMeasurements2());

        return Arrays.asList(cityFeDto1,cityFeDto2);
    }
}
