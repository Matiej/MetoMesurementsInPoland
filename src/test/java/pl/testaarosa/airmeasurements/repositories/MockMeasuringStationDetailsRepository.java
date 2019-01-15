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
                .voivodeship("mazowieckie")
                .build();
        MeasuringStationDetails details2 = new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .id(2L)
                .city("Poznan")
                .commune("Inne Commune1")
                .district("Dictrict1")
                .voivodeship("wielkopolskie")
                .build();
        MeasuringStationDetails details3 = new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .id(3L)
                .city("Krakow")
                .commune("Inne Commune1")
                .district("Dictrict1")
                .voivodeship("malopolskie")
                .build();
        detailsList.add(details1);
        detailsList.add(details2);
        detailsList.add(details3);
        return detailsList;
    }
}
