package pl.testaarosa.airmeasurements.services;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

public interface AddMeasurementsService {
    String addMeasurements(int statioId) throws ExecutionException, InterruptedException;

    String addMeasurementsAllStations() throws ExecutionException, InterruptedException;
}
