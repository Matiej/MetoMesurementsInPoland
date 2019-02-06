package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.util.List;
import java.util.NoSuchElementException;

public interface AddMeasurementsService {

    List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException;
    MeasuringStation addOneStationMeasurement(Integer stationId) throws NumberFormatException,RestClientException,
    HibernateException, NoSuchElementException;
}
