package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MockSynopticMeasurementRepository {

    private final MockCityRepository mockCityRepository = new MockCityRepository();
    public List<SynopticMeasurement> synopticMeasurementsOrderColdest(){
        List<City> cities = mockCityRepository.cityList();
        LocalDateTime saveDate = LocalDateTime.of(2018,05,05,12,01,05).withNano(0);
        LocalDateTime measurementdate = LocalDateTime.of(2018,02,22,18,00,00).withNano(0);
        SynopticMeasurement synopticMeasurement1 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(saveDate)
                .temperature(6)
                .windSpeed(35)
                .airHumidity(77)
                .pressure(999)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement1.setCity(cities.get(0));

        SynopticMeasurement synopticMeasurement2 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(saveDate)
                .temperature(6)
                .windSpeed(35)
                .airHumidity(77)
                .pressure(999)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement2.setCity(cities.get(0));

        SynopticMeasurement synopticMeasurement3 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(2L)
                .foreignId(2)
                .city("Poznan")
                .saveDate(saveDate)
                .temperature(12)
                .windSpeed(35)
                .airHumidity(66)
                .pressure(999)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement3.setCity(cities.get(1));

        SynopticMeasurement synopticMeasurement4 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(3)
                .city("Krakow")
                .saveDate(saveDate)
                .temperature(15)
                .windSpeed(35)
                .airHumidity(66)
                .pressure(999)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement4.setCity(cities.get(2));

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        synopticMeasurementList.add(synopticMeasurement1);
        synopticMeasurementList.add(synopticMeasurement2);
        synopticMeasurementList.add(synopticMeasurement3);
        synopticMeasurementList.add(synopticMeasurement4);
        return synopticMeasurementList;
    }

    public List<SynopticMeasurement> synopticMeasurementsOrderHottest(){
        List<City> cities = mockCityRepository.cityList();
        LocalDateTime date = LocalDateTime.of(2018,05,11,10,20);
        LocalDateTime measurementdate = LocalDateTime.of(2018,02,22,18,00,00).withNano(0);
        SynopticMeasurement synopticMeasurement1 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(28)
                .windSpeed(15)
                .airHumidity(16)
                .pressure(1001)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement1.setCity(cities.get(0));

        SynopticMeasurement synopticMeasurement2 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(1L)
                .foreignId(1)
                .city("Warszawa")
                .saveDate(date)
                .temperature(28)
                .windSpeed(15)
                .airHumidity(16)
                .pressure(1001)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement2.setCity(cities.get(0));

        SynopticMeasurement synopticMeasurement3 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(2L)
                .foreignId(2)
                .city("Poznan")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement3.setCity(cities.get(1));

        SynopticMeasurement synopticMeasurement4 = new SynopticMeasurement.SynopticMeasurementsBuilder()
                .id(3L)
                .foreignId(3)
                .city("Krakow")
                .saveDate(date)
                .temperature(19)
                .windSpeed(11)
                .airHumidity(26)
                .pressure(999)
                .measurementDate(measurementdate)
                .build();
        synopticMeasurement4.setCity(cities.get(2));

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        synopticMeasurementList.add(synopticMeasurement1);
        synopticMeasurementList.add(synopticMeasurement2);
        synopticMeasurementList.add(synopticMeasurement3);
        synopticMeasurementList.add(synopticMeasurement4);
        return synopticMeasurementList;
    }

    public LinkedHashMap<String, SynopticMeasurement> measurementMap() {
        LinkedHashMap<String, SynopticMeasurement> measurementLinkedHashMap = new LinkedHashMap<>();
        synopticMeasurementsOrderHottest().forEach(t-> measurementLinkedHashMap.put(t.getCityName(), t));
        synopticMeasurementsOrderColdest().forEach(t-> measurementLinkedHashMap.put(t.getCityName(), t));
        return measurementLinkedHashMap;
    }
}
