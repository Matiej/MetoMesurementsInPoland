package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.City;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockAirMeasurementRepository {

    private final MockCityRepository mockCityRepository = new MockCityRepository();

    public List<AirMeasurement> airMeasurements1() {
        LocalDateTime date = LocalDateTime.of(2018, 05, 05, 12,01,05);
        List<AirMeasurement> result = new ArrayList<>();
        List<City> cityList = mockCityRepository.cityList();
        AirMeasurement airMeasurement1 = new AirMeasurement.AirMaesurementsBuilder()
                .airQuality(AirMeasurementLevel.VERY_BAD)
                .stIndexLevel("Very good")
                .c6h6IndexLevel("Good")
                .coIndexLevel("Sufficient")
                .no2IndexLevel("Moderate")
                .o3IndexLevel("Very good")
                .pm10IndexLevel("Bad")
                .pm25IndexLevel("Very bad")
                .so2IndexLevel("Good")
                .foreignId(1)
                .measurementDate(date)
                .saveDate(date)
                .build();
        airMeasurement1.setCity(cityList.get(0));

        AirMeasurement airMeasurement2 = new AirMeasurement.AirMaesurementsBuilder()
                .airQuality(AirMeasurementLevel.MODERATE)
                .c6h6IndexLevel("c6hLEVEL")
                .coIndexLevel("coLEVEL")
                .no2IndexLevel("noLEVEL")
                .o3IndexLevel("o3LEVEL")
                .pm10IndexLevel("pm10LEVEL")
                .pm25IndexLevel("pm25LEVEL")
                .so2IndexLevel("so2LEVEL")
                .foreignId(2)
                .measurementDate(date)
                .saveDate(date)
                .build();
        airMeasurement2.setCity(cityList.get(0));
        result.add(airMeasurement1);
        result.add(airMeasurement2);
        return result;
    }

    public List<AirMeasurement> airMeasurements2() {
        LocalDateTime date = LocalDateTime.of(2018, 05, 11, 10, 20,20);
        List<AirMeasurement> result = new ArrayList<>();
        List<City> cityList = mockCityRepository.cityList();
        AirMeasurement airMeasurement3 = new AirMeasurement.AirMaesurementsBuilder()
                .airQuality(AirMeasurementLevel.SUFFICIENT)
                .c6h6IndexLevel("c6hLEVEL")
                .coIndexLevel("coLEVEL")
                .no2IndexLevel("noLEVEL")
                .o3IndexLevel("o3LEVEL")
                .pm10IndexLevel("pm10LEVEL")
                .pm25IndexLevel("pm25LEVEL")
                .so2IndexLevel("so2LEVEL")
                .foreignId(3)
                .measurementDate(date)
                .saveDate(date)
                .build();
        airMeasurement3.setCity(cityList.get(0));

        AirMeasurement airMeasurement4 = new AirMeasurement.AirMaesurementsBuilder()
                .airQuality(AirMeasurementLevel.BAD)
                .c6h6IndexLevel("c6hLEVEL2")
                .coIndexLevel("coLEVEL2")
                .no2IndexLevel("noLEVEL2")
                .o3IndexLevel("o3LEVEL2")
                .pm10IndexLevel("pm10LEVEL2")
                .pm25IndexLevel("pm25LEVEL2")
                .so2IndexLevel("so2LEVEL2")
                .foreignId(4)
                .measurementDate(date)
                .saveDate(date)
                .build();
        airMeasurement4.setCity(cityList.get(1));

        AirMeasurement airMeasurement5 = new AirMeasurement.AirMaesurementsBuilder()
                .airQuality(AirMeasurementLevel.BAD)
                .c6h6IndexLevel("c6hLEVEL2")
                .coIndexLevel("coLEVEL2")
                .no2IndexLevel("noLEVEL2")
                .o3IndexLevel("o3LEVEL2")
                .pm10IndexLevel("pm10LEVEL2")
                .pm25IndexLevel("pm25LEVEL2")
                .so2IndexLevel("so2LEVEL2")
                .foreignId(44)
                .measurementDate(date)
                .saveDate(date)
                .build();
        airMeasurement5.setCity(cityList.get(2));
        result.add(airMeasurement3);
        result.add(airMeasurement4);
        result.add(airMeasurement5);
        return result;
    }
}