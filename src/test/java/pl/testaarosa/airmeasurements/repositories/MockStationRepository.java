package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;

import java.util.ArrayList;
import java.util.List;

public class MockStationRepository {

    public List<MeasuringStation> stations(){
        MockAirRepository mockAirRepository = new MockAirRepository();
        MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();
        List<MeasuringStation> result = new ArrayList<>();

        MeasuringStation station1 = new MeasuringStation();
        station1.setId(1L);
        station1.setStationId(1);
        station1.setStationName("Wawrszawa-Centrum");
        station1.setLatitude("15");
        station1.setLongitude("15");
        station1.setStreet("Piekna");
        station1.setCity("Warszawa");
        station1.setStationDetails(new MeasuringStationDetails());
        station1.setAirMeasurementsList(mockAirRepository.airMeasurements1());
        station1.setSynopticMeasurements(mockSynopticRepository.synopticMeasurements1());

        MeasuringStation station2 = new MeasuringStation();
        station2.setId(1L);
        station2.setStationId(1);
        station2.setStationName("Szczecin-Centrum");
        station2.setLatitude("15");
        station2.setLongitude("15");
        station2.setStreet("Morska");
        station2.setCity("Szczecin");
        station2.setStationDetails(new MeasuringStationDetails());
        station2.setAirMeasurementsList(mockAirRepository.airMeasurements2());
        station2.setSynopticMeasurements(mockSynopticRepository.synopticMeasurements2());

        result.add(station1);
        result.add(station2);
        return result;
    }

    public MeasuringStation station(){
        MeasuringStation station = new MeasuringStation();
        station.setId(1L);
        station.setStationId(1);
        station.setStationName("");
        station.setLatitude("");
        station.setLongitude("");
        station.setStreet("");
        station.setCity("");
        station.setStationDetails(new MeasuringStationDetails());
        station.setAirMeasurementsList(new ArrayList<>());
        station.setSynopticMeasurements(new ArrayList<>());
        return station;
    }
}
