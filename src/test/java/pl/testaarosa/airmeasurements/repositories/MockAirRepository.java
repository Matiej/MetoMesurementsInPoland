package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockAirRepository {

    public List<AirMeasurements> airMeasurements1(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01,05);
        List<AirMeasurements> result = new ArrayList<>();
        AirMeasurements airMeasurements1 = new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.GOOD)
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

        AirMeasurements airMeasurements2 = new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.MODERATE)
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
        result.add(airMeasurements1);
        result.add(airMeasurements2);
        return result;
    }

    public List<AirMeasurements> airMeasurements2(){
        LocalDateTime date = LocalDateTime.of(2018,05,11,10,20);
        List<AirMeasurements> result = new ArrayList<>();
        AirMeasurements airMeasurements3 = new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.SUFFICIENT)
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

        AirMeasurements airMeasurements4 = new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.BAD)
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
        AirMeasurements airMeasurements5 = new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.BAD)
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
        result.add(airMeasurements3);
        result.add(airMeasurements4);
        result.add(airMeasurements5);
        return result;
    }

    public AirMeasurements airMeasurement(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01,05);
        LocalDateTime currentDate = LocalDateTime.now();
        return new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.GOOD)
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

    public AirMeasurements airMeasurement1(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01,05);
        LocalDateTime currentDate = LocalDateTime.now();
        return new AirMeasurements.AirMaesurementsBuilder()
                .airQuality(MeasurementsAirLevel.GOOD)
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