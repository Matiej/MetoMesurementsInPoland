package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MockAirMeasurementRepository {

    public List<AirMeasurement> airMeasurements1(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01,05);
        List<AirMeasurement> result = new ArrayList<>();
        AirMeasurement airMeasurement1 = new AirMeasurement.AirMaesurementsBuilder()
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
        result.add(airMeasurement1);
        result.add(airMeasurement2);
        return result;
    }

    public List<AirMeasurement> airMeasurements2(){
        LocalDateTime date = LocalDateTime.of(2018,05,11,10,20);
        List<AirMeasurement> result = new ArrayList<>();
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
        result.add(airMeasurement3);
        result.add(airMeasurement4);
        result.add(airMeasurement5);
        return result;
    }

    public AirMeasurement airMeasurement(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01,05);
        LocalDateTime currentDate = LocalDateTime.now();
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
                .saveDate(currentDate.withNano(0))
                .build();
    }
}