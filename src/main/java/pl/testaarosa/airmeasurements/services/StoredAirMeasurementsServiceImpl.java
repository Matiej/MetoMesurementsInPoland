package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementDao;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementDaoImpl;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StoredAirMeasurementsServiceImpl implements StoredAirMeasurementsService {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AirMeasurementDao repository;

    @Autowired
    public StoredAirMeasurementsServiceImpl(AirMeasurementDao repository) {
        this.repository = repository;
    }

    @Override
    public List<AirMeasurement> getAirMeasurementsByLevel(AirMeasurementLevel airMeasurementLevel) throws IllegalArgumentException,
            NoSuchElementException, DataIntegrityViolationException {
        List<AirMeasurement> allByAirQuality;
        if (isMeasurementLevelValid(airMeasurementLevel)) {
            try {
                allByAirQuality = repository.findAllAirMeasurementsByAirQuality(airMeasurementLevel);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("No enum constant " + airMeasurementLevel);
        }
        if (allByAirQuality.isEmpty()) {
            throw new NoSuchElementException("There are no measurements for given air level: " + airMeasurementLevel);
        }
        return allByAirQuality;
    }

    @Override
    public List<AirMeasurement> getAirMeasurementsByDate(String date) throws NoSuchElementException, DateTimeException, DataIntegrityViolationException {
        List<AirMeasurement> airMeasurementList = new ArrayList<>();
        if (isValidDate(date)) {
            try {
                DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime localDate = LocalDateTime.parse(date + " 00:00:00", localDateTimeFormatter);
               airMeasurementList = repository.getAirMeasurementsBySaveDate(localDate);
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db connection problem: " + e.getMessage());
            }
        } else if (!isValidDate(date)) {
            throw new DateTimeException("Wrong date format!");
        }
        if (airMeasurementList.isEmpty()) {
            throw new NoSuchElementException("Cant't find any air measurements for date: " + date);
        }
        return airMeasurementList;
    }

    private boolean isValidDate(String date) {
        if (date.isEmpty() || !date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        } else {
            try {
                formatter.parse(date);
                return true;
            } catch (DateTimeException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private boolean isMeasurementLevelValid(AirMeasurementLevel airMeasurementLevel) {
        return Arrays.asList(AirMeasurementLevel.values()).contains(airMeasurementLevel);
    }
}
