package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MockSynopticRepository {

    public List<SynopticMeasurements> synopticMeasurements1(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01);
        SynopticMeasurements synopticMeasurements = new SynopticMeasurements.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(100)
                .city("Warszawa")
                .saveDate(date)
                .temperature(6)
                .windSpeed(35)
                .airHumidity(77)
                .pressure(999)
                .build();

        SynopticMeasurements synopticMeasurements1 = new SynopticMeasurements.SynopticMeasurementsBuilder()
                .id(2L)
                .foreignId(110)
                .city("Warszawa")
                .saveDate(date)
                .temperature(11)
                .windSpeed(25)
                .airHumidity(16)
                .pressure(888)
                .build();

        SynopticMeasurements synopticMeasurements2 = new SynopticMeasurements.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(130)
                .city("Warszawa")
                .saveDate(date)
                .temperature(11)
                .windSpeed(35)
                .airHumidity(66)
                .pressure(999)
                .build();

        List<SynopticMeasurements> synopticMeasurementsList = new ArrayList<>();
        synopticMeasurementsList.add(synopticMeasurements);
        synopticMeasurementsList.add(synopticMeasurements1);
        synopticMeasurementsList.add(synopticMeasurements2);
        return synopticMeasurementsList;
    }

    public List<SynopticMeasurements> synopticMeasurements2(){
        LocalDateTime date = LocalDateTime.of(2018,05,11,10,20);
        SynopticMeasurements synopticMeasurements = new SynopticMeasurements.SynopticMeasurementsBuilder()
                .id(11L)
                .foreignId(111)
                .city("Szczecien1")
                .saveDate(date)
                .temperature(28)
                .windSpeed(15)
                .airHumidity(16)
                .pressure(1001)
                .build();

        SynopticMeasurements synopticMeasurements1 = new SynopticMeasurements.SynopticMeasurementsBuilder()
                .id(12L)
                .foreignId(112)
                .city("Szczecin2")
                .saveDate(date)
                .temperature(22)
                .windSpeed(11)
                .airHumidity(16)
                .pressure(999)
                .build();

        SynopticMeasurements synopticMeasurements2 = new SynopticMeasurements.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(133)
                .city("Szczecin3")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .build();

        List<SynopticMeasurements> synopticMeasurementsList = new ArrayList<>();
        synopticMeasurementsList.add(synopticMeasurements);
        synopticMeasurementsList.add(synopticMeasurements1);
        synopticMeasurementsList.add(synopticMeasurements2);
        return synopticMeasurementsList;
    }
}
