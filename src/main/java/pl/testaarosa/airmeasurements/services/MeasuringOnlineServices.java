package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;

import java.util.List;

public interface MeasuringOnlineServices {
    List<MeasuringStationOnLine> getAllMeasuringStations();

    List<MeasuringStationOnLine> getGivenCityMeasuringStationsWithSynopticData(String cityName);

    MeasuringStationOnLine getHottestOnlineStation();

    MeasuringStationOnLine getColdestOnlineStation();

    void addAllStations();
}
