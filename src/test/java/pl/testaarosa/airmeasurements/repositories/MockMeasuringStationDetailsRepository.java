package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;

public class MockMeasuringStationDetailsRepository {

    public MeasuringStationDetails details1(){
        return new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .id(1L)
                .city("City")
                .commune("Commune1")
                .district("Dictrict1")
                .voivodeship("voivodeship1")
                .build();
    }

    public MeasuringStationDetails details2(){
        return new MeasuringStationDetails.MeasuringStationDetailsBuilder()
                .id(1L)
                .city("City")
                .commune("Commune1")
                .district("Dictrict1")
                .voivodeship("voivodeship1")
                .build();
    }
}
