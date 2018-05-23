package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.domain.*;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;

import java.util.List;

public interface GetMeasurementsService {
    List<MeasuringStation> findAll();

    List<AirMeasurements> getAirMeasurements(String date);

    List<AirMeasurements> getAirMeasurements(MeasurementsAirLevel measurementsAirLevel);

    List<SynopticMeasurements> getSynopticMeasuremets(String date);

    SynopticMeasurements getHottestPlaceGivenDate(String date);

    SynopticMeasurements getColdestPlaceGivenDate(String date);

    List<SynopticMeasurements> getColdestPlaces();

    List<SynopticMeasurements> getHottestPlaces();

}
