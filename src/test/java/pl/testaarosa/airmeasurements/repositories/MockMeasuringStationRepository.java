package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MockMeasuringStationRepository {
    private final MockAirMeasurementRepository mockAirMeasurementRepository = new MockAirMeasurementRepository();
    private final MockSynopticMeasurementRepository mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
    private final MockMeasuringStationDetailsRepository details = new MockMeasuringStationDetailsRepository();

    public List<MeasuringStation> stations() {
        List<MeasuringStation> result = new ArrayList<>();

        MeasuringStation station1 = new MeasuringStation();
        station1.setId(1L);
        station1.setStationId(1);
        station1.setStationName("Wawrszawa-Centrum");
        station1.setLatitude("15");
        station1.setLongitude("15");
        station1.setStreet("Piekna");
        station1.setCity("Warszawa");
        station1.setStationDetails(details.detailsList().get(0));
        station1.setAirMeasurementList(mockAirMeasurementRepository.airMeasurements1());
        station1.setSynopticMeasurements(mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest());

        MeasuringStation station2 = new MeasuringStation();
        station2.setId(1L);
        station2.setStationId(1);
        station2.setStationName("Wawrszawa-Centrum");
        station2.setLatitude("15");
        station2.setLongitude("15");
        station2.setStreet("Piekna");
        station2.setCity("Warszawa");
        station2.setStationDetails(details.detailsList().get(0));
        station2.setAirMeasurementList(mockAirMeasurementRepository.airMeasurements1());
        station2.setSynopticMeasurements(mockSynopticMeasurementRepository.synopticMeasurementsOrderColdest());

        MeasuringStation station3 = new MeasuringStation();
        station3.setId(2L);
        station3.setStationId(2);
        station3.setStationName("Poznan-Stoleczna");
        station3.setLatitude("11");
        station3.setLongitude("123");
        station3.setStreet("Stoleczna");
        station3.setCity("Poznan");
        station3.setStationDetails(details.detailsList().get(1));
        station3.setAirMeasurementList(mockAirMeasurementRepository.airMeasurements2());
        station3.setSynopticMeasurements(mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest());

        MeasuringStation station4 = new MeasuringStation();
        station4.setId(3L);
        station4.setStationId(3);
        station4.setStationName("Krakow-Starowka");
        station4.setLatitude("23");
        station4.setLongitude("221");
        station4.setStreet("Starowka");
        station4.setCity("Krakow");
        station4.setStationDetails(details.detailsList().get(2));
        station4.setAirMeasurementList(mockAirMeasurementRepository.airMeasurements2());
        station4.setSynopticMeasurements(mockSynopticMeasurementRepository.synopticMeasurementsOrderHottest());

        result.add(station1);
        result.add(station2);
        result.add(station3);
        result.add(station4);
        return result;

    }

    public LinkedHashMap<MeasuringStation, AirMeasurement> measurementMap() {
        List<MeasuringStation> stations = stations();
        LinkedHashMap<MeasuringStation, AirMeasurement> measurementLinkedHashMap = new LinkedHashMap<>();
        stations.remove(0);
        measurementLinkedHashMap.put(stations.get(0), stations.get(0).getAirMeasurementList().get(0));
        measurementLinkedHashMap.put(stations.get(1), stations.get(1).getAirMeasurementList().get(1));
        measurementLinkedHashMap.put(stations.get(2), stations.get(2).getAirMeasurementList().get(2));
        return measurementLinkedHashMap;
//        return stations.stream().collect(Collectors.toMap(t->t, t->t.getAirMeasurementList().get(0), (t1,t2)->t1,LinkedHashMap::new));
    }
}
