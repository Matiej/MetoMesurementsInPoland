package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MeasuringOnlineServices {
    List<MeasuringStationOnLine> getAllMeasuringStations() throws ExecutionException, InterruptedException;

    List<MeasuringStationOnLine> getGivenCityMeasuringStationsWithSynopticData(String cityName) throws ExecutionException, InterruptedException;

    MeasuringStationOnLine getHottestOnlineStation() throws ExecutionException, InterruptedException;

    MeasuringStationOnLine getColdestOnlineStation() throws ExecutionException, InterruptedException;

    void addAllStations() throws ExecutionException, InterruptedException;
}
