package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MockSynopticRepository {

    public List<SynopticMeasurement> synopticMeasurementsOrderColdest(){
        LocalDateTime date = LocalDateTime.of(2018,05,05,12,01).withNano(0);
        SynopticMeasurement synopticMeasurement1 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(6)
                .windSpeed(35)
                .airHumidity(77)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement2 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(6)
                .windSpeed(35)
                .airHumidity(77)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement3 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(2L)
                .foreignId(2)
                .city("Poznan")
                .saveDate(date)
                .temperature(12)
                .windSpeed(35)
                .airHumidity(66)
                .pressure(999)
                .build();

        SynopticMeasurement synopticMeasurement4 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(3)
                .city("Krakow")
                .saveDate(date)
                .temperature(15)
                .windSpeed(35)
                .airHumidity(66)
                .pressure(999)
                .build();

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        synopticMeasurementList.add(synopticMeasurement1);
        synopticMeasurementList.add(synopticMeasurement2);
        synopticMeasurementList.add(synopticMeasurement3);
        synopticMeasurementList.add(synopticMeasurement4);
        return synopticMeasurementList;
    }

    public List<SynopticMeasurement> synopticMeasurementsOrderHottest(){
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
                .temperature(22)
                .windSpeed(11)
                .airHumidity(16)
                .pressure(999)
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

    public Map<String, SynopticMeasurement> measurementMap() {
        LinkedHashMap<String, SynopticMeasurement> measurementLinkedHashMap = new LinkedHashMap<>();
        synopticMeasurementsOrderHottest().forEach(t-> measurementLinkedHashMap.put(t.getCity(), t));
        synopticMeasurementsOrderColdest().forEach(t-> measurementLinkedHashMap.put(t.getCity(), t));
        return measurementLinkedHashMap;
    }
}
