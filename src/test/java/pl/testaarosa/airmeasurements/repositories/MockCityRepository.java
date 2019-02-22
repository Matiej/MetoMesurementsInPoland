package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockCityRepository {

//    private MockAirMeasurementRepository airMeasurementRepository;
//    private MockSynopticMeasurementRepository synopticMeasurementRepository;

    public MockCityRepository() {
//        airMeasurementRepository = new MockAirMeasurementRepository();
//        synopticMeasurementRepository = new MockSynopticMeasurementRepository();
    }

    public List<City> cityList() {
//        List<AirMeasurement> airMeasurementList1 = airMeasurementRepository.airMeasurements1();
//        List<AirMeasurement> airMeasurementList2 = airMeasurementRepository.airMeasurements1();
//        List<SynopticMeasurement> hot = synopticMeasurementRepository.synopticMeasurementsOrderHottest();
//        List<SynopticMeasurement> cold = synopticMeasurementRepository.synopticMeasurementsOrderColdest();
//        AirMeasurement airMeasurement = airMeasurementRepository.airMeasurement();
        List<AirMeasurement> airL = new ArrayList<>();
        airL.add(airMeasurement());
        List<City> mockCityList = new ArrayList<>();

        City warszawa = new City();
        warszawa.setCityName("Warszawa");
        warszawa.setAirMeasurementList(airL);
        warszawa.setSynopticMeasurementList(synopticMeasurementsOrderHottest());

        City poznan = new City();
        poznan.setCityName("Poznan");
        poznan.setAirMeasurementList(airL);
        poznan.setSynopticMeasurementList(synopticMeasurementsOrderHottest());

        City krakow = new City();
        krakow.setCityName("Krakow");
        krakow.setAirMeasurementList(airL);
        krakow.setSynopticMeasurementList(synopticMeasurementsOrderHottest());

        mockCityList.add(warszawa);
        mockCityList.add(poznan);
        mockCityList.add(krakow);
        return mockCityList;
    }

    private AirMeasurement airMeasurement() {
        LocalDateTime date = LocalDateTime.of(2018, 05, 05, 12, 01, 05);
        return new AirMeasurement.AirMaesurementsBuilder()
                .airQuality(AirMeasurementLevel.GOOD)
                .stIndexLevel("stILevel")
                .c6h6IndexLevel("c6hLEVEL")
                .coIndexLevel("coLEVEL")
                .no2IndexLevel("noLEVEL")
                .o3IndexLevel("o3LEVEL")
                .pm10IndexLevel("pm10LEVEL")
                .pm25IndexLevel("pm25LEVEL")
                .so2IndexLevel("so2LEVEL")
                .foreignId(1)
                .measurementDate(date)
                .saveDate(date)
                .build();
    }

    private List<SynopticMeasurement> synopticMeasurementsOrderHottest(){
        LocalDateTime date = LocalDateTime.of(2018,05,11,10,20);
        SynopticMeasurement synopticMeasurement1 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(28)
                .windSpeed(15)
                .airHumidity(16)
                .pressure(1001)
                .build();

        SynopticMeasurement synopticMeasurement2 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(28)
                .windSpeed(15)
                .airHumidity(16)
                .pressure(1001)
                .build();

        SynopticMeasurement synopticMeasurement3 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(2L)
                .foreignId(2)
                .city("Poznan")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement4 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(3)
                .city("Krakow")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .build();

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        synopticMeasurementList.add(synopticMeasurement1);
        synopticMeasurementList.add(synopticMeasurement2);
        synopticMeasurementList.add(synopticMeasurement3);
        synopticMeasurementList.add(synopticMeasurement4);
        return synopticMeasurementList;
    }
}
