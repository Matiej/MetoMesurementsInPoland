package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.ArrayList;
import java.util.List;

public class MockCityRepository {

    private MockAirMeasurementRepository airMeasurementRepository;
    private MockSynopticMeasurementRepository synopticMeasurementRepository;

    public MockCityRepository() {
        airMeasurementRepository = new MockAirMeasurementRepository();
        synopticMeasurementRepository = new MockSynopticMeasurementRepository();
    }

    public List<City> cityList() {
        List<AirMeasurement> airMeasurementList1 = airMeasurementRepository.airMeasurements1();
        List<AirMeasurement> airMeasurementList2 = airMeasurementRepository.airMeasurements1();
        List<SynopticMeasurement> hot = synopticMeasurementRepository.synopticMeasurementsOrderHottest();
        List<SynopticMeasurement> cold = synopticMeasurementRepository.synopticMeasurementsOrderColdest();

        List<City> mockCityList = new ArrayList<>();

        City warszawa = new City();
        warszawa.setCityName("Warszawa");
        warszawa.setAirMeasurementList(airMeasurementList1);
        warszawa.setSynopticMeasurementList(cold);

        City poznan = new City();
        poznan.setCityName("Poznan");
        poznan.setAirMeasurementList(airMeasurementList2);
        poznan.setSynopticMeasurementList(hot);

        City krakow = new City();
        krakow.setCityName("Krakow");
        krakow.setAirMeasurementList(airMeasurementList2);
        krakow.setSynopticMeasurementList(hot);

        mockCityList.add(warszawa);
        mockCityList.add(poznan);
        mockCityList.add(krakow);
        return mockCityList;
    }
}
