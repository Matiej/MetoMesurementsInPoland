package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface AddMeasurementsService {

    List<MeasuringStation> addMeasurementsAllStations() throws ExecutionException, InterruptedException;
    MeasuringStation addOne(Integer stationId);

}
