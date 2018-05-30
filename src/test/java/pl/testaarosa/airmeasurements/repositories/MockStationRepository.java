package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;

import java.util.ArrayList;
import java.util.List;

public class MockStationRepository {
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockSynopticRepository mockSynopticRepository = new MockSynopticRepository();
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
        station1.setAirMeasurementsList(mockAirRepository.airMeasurements1());
        station1.setSynopticMeasurements(mockSynopticRepository.synopticMeasurements1());

        MeasuringStation station2 = new MeasuringStation();
        station2.setId(1L);
        station2.setStationId(2);
        station2.setStationName("Poznan");
        station2.setLatitude("15");
        station2.setLongitude("15");
        station2.setStreet("Morska");
        station2.setCity("Poznan");
        station2.setStationDetails(details.detailsList().get(0));
        station2.setAirMeasurementsList(mockAirRepository.airMeasurements2());
        station2.setSynopticMeasurements(mockSynopticRepository.synopticMeasurements2());

        MeasuringStation station3 = new MeasuringStation();
        station3.setId(1L);
        station3.setStationId(3);
        station3.setStationName("Krakow");
        station3.setLatitude("15");
        station3.setLongitude("15");
        station3.setStreet("Morska");
        station3.setCity("Krakow");
        station3.setStationDetails(details.detailsList().get(0));
        station3.setAirMeasurementsList(mockAirRepository.airMeasurements2());
        station3.setSynopticMeasurements(mockSynopticRepository.synopticMeasurements2());

        MeasuringStation station4 = new MeasuringStation();
        station4.setId(1L);
        station4.setStationId(3);
        station4.setStationName("Krakow");
        station4.setLatitude("15");
        station4.setLongitude("15");
        station4.setStreet("Morska");
        station4.setCity("Krakow");
        station4.setStationDetails(details.detailsList().get(0));
        station4.setAirMeasurementsList(mockAirRepository.airMeasurements2());
        station4.setSynopticMeasurements(mockSynopticRepository.synopticMeasurements2());

        result.add(station1);
        result.add(station2);
        result.add(station3);
        result.add(station4);
        return result;
    }

    public MeasuringStation stationForMapperTest(){
        MeasuringStation station1 = new MeasuringStation();
        station1.setStationId(1);
        station1.setStationName("Wawrszawa-Centrum");
        station1.setLatitude("15");
        station1.setLongitude("15");
        station1.setStreet("Piekna");
        station1.setCity("Warszawa");

        return station1;
    }
}
