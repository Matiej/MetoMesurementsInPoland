package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockSynopticRepository {

    public List<SynopticMeasurement> synopticMeasurementsOrderColdest(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01).withNano(0);
        SynopticMeasurement synopticMeasurement = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(11)
                .city("Warszawa")
                .saveDate(date)
                .temperature(6)
                .windSpeed(35)
                .airHumidity(77)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement1 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(2L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(11)
                .windSpeed(50)
                .airHumidity(77)
                .pressure(1001)
                .build();

        SynopticMeasurement synopticMeasurement2 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(130)
                .city("Warszawa")
                .saveDate(date)
                .temperature(12)
                .windSpeed(35)
                .airHumidity(66)
                .pressure(999)
                .build();

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        synopticMeasurementList.add(synopticMeasurement);
        synopticMeasurementList.add(synopticMeasurement1);
        synopticMeasurementList.add(synopticMeasurement2);
        return synopticMeasurementList;
    }

    public List<SynopticMeasurement> synopticMeasurementsOrderHottest(){
        LocalDateTime date = LocalDateTime.of(2018,05,11,10,20);
        SynopticMeasurement synopticMeasurement = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(11L)
                .foreignId(111)
                .city("Szczecien1")
                .saveDate(date)
                .temperature(28)
                .windSpeed(15)
                .airHumidity(16)
                .pressure(1001)
                .build();

        SynopticMeasurement synopticMeasurement1 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(12L)
                .foreignId(112)
                .city("Szczecin2")
                .saveDate(date)
                .temperature(22)
                .windSpeed(11)
                .airHumidity(16)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement2 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(133)
                .city("Szczecin3")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement3 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(133)
                .city("Szczecin3")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .build();

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        synopticMeasurementList.add(synopticMeasurement);
        synopticMeasurementList.add(synopticMeasurement1);
        synopticMeasurementList.add(synopticMeasurement2);
        synopticMeasurementList.add(synopticMeasurement3);
        return synopticMeasurementList;
    }
}
