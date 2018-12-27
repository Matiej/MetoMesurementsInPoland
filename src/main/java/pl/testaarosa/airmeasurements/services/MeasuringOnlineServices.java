package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public interface MeasuringOnlineServices {
    List<MeasuringStationOnLine> getAllMeasuringStations() throws ExecutionException, InterruptedException, NoSuchElementException;

    List<MeasuringStationOnLine> getGivenCityMeasuringStationsWithSynopticData(String cityName)
            throws ExecutionException, InterruptedException, IllegalArgumentException, NoSuchElementException;

    MeasuringStationOnLine getHottestOnlineStation() throws ExecutionException, InterruptedException, NoSuchElementException;

    MeasuringStationOnLine getColdestOnlineStation() throws ExecutionException, InterruptedException;

    List<MeasuringStation> addAllStations() throws ExecutionException, InterruptedException;
}
