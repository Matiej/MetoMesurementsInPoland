package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;

import java.util.ArrayList;
import java.util.List;

public class MockMeasuringStationDetailsRepository {

    public List<MeasuringStationDetails>    detailsList() {

        List<MeasuringStationDetails> detailsList = new ArrayList<>();
        MeasuringStationDetails details1 = new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .id(1L)
                .city("Warszawa")
                .commune("Commune1")
                .district("Dictrict1")
                .voivodeship("voivodeship1")
                .build();
        MeasuringStationDetails details2 = new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .id(1L)
                .city("Inne CITY")
                .commune("Inne Commune1")
                .district("Inne Dictrict1")
                .voivodeship("Inne voivodeship1")
                .build();
        detailsList.add(details1);
        detailsList.add(details2);
        return detailsList;
    }
}
