package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.hibernate.TransactionException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

public interface AddMeasurementsService {

    List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException;
    MeasuringStation addOneStationMeasurement(Integer stationId) throws NumberFormatException,RestClientException,
    HibernateException, NoSuchElementException;
}
