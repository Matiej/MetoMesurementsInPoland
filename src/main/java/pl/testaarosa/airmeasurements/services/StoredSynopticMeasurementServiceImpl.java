package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StoredSynopticMeasurementServiceImpl implements StoredSynopticMeasurementService{

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final SynopticMeasurementRepository synopticRepository;

    @Autowired
    public StoredSynopticMeasurementServiceImpl(SynopticMeasurementRepository synopticRepository) {
        this.synopticRepository = synopticRepository;
    }

    @Override
    public List<SynopticMeasurement> findAll() throws NoSuchElementException, DataIntegrityViolationException {
        try {
            List<SynopticMeasurement> measuringStationList = synopticRepository.findAll();
            if (measuringStationList.isEmpty()) {
                throw new NoSuchElementException("Can't find any synoptic measurements in data base");
            } else {
                return measuringStationList;
            }
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("There is some db problem: " + e.getMessage());
        }
    }

    @Override
    public List<SynopticMeasurement> getSynopticMeasuremets(String date) throws NoSuchElementException, DateTimeException, DataIntegrityViolationException {

        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        if (isValidDate(date)) {
            try {
                LocalDateTime saveDate = LocalDateTime.parse(date+ " 00:00:00", dateTimeFormatter);
                synopticMeasurementList = synopticRepository.findSynopticMeasurementsByDate(saveDate, saveDate.plusDays(1));
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db connection problem: " + e.getMessage());
            }
        } else if (!isValidDate(date)) {
            throw new DateTimeException("Wrong date format!");
        }
        if (synopticMeasurementList.isEmpty()) {
            throw new NoSuchElementException("Cant't find any synoptic measurements for date: " + date);
        }
        return synopticMeasurementList;
    }


    @Override
    public List<SynopticMeasurement> getHottestPlaces(String noOfResults) throws NoSuchElementException, DataIntegrityViolationException {
        List<SynopticMeasurement> measurementsList;
        if(isValidNoOfResults(noOfResults)) {
            try {
                measurementsList = synopticRepository.findHottestPlaces(PageRequest.of(0, Integer.parseInt(noOfResults)));
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
            if (measurementsList.isEmpty()) {
                throw new NoSuchElementException("Can't find hottest " + noOfResults + " measurements");
            } else {
                return measurementsList;
            }
        } else {
            throw new IllegalArgumentException("Wrong argument, " + noOfResults + " is not a number");
        }
    }

    @Override
    public List<SynopticMeasurement> getColdestPlaces(String noOfResults) throws NoSuchElementException, DataIntegrityViolationException {
        List<SynopticMeasurement> measurementsList;
        if(isValidNoOfResults(noOfResults)) {
            try {
                measurementsList = synopticRepository.findColdestPlaces(PageRequest.of(0, Integer.parseInt(noOfResults)));
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
            if (measurementsList.isEmpty()) {
                throw new NoSuchElementException("Can't find coldest " + noOfResults + " measurements");
            } else {
                return measurementsList;
            }
        } else {
            throw new IllegalArgumentException("Wrong argument, " + noOfResults + " is not a number");
        }
    }

    @Override
    public SynopticMeasurement getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException {
        LocalDateTime saveDate = LocalDateTime.parse(date+ " 00:00:00", dateTimeFormatter);
        SynopticMeasurement synopticMeasurement;
        if (isValidDate(date)) {
            try {
                synopticMeasurement = synopticRepository.findHottestPlacesByDate(saveDate, saveDate.plusDays(1));
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
        } else {
            throw new DateTimeException("Wrong date format!");
        }
        if (!Optional.ofNullable(synopticMeasurement).isPresent()) {
            throw new NoSuchElementException("Cant't find any synoptic measurements for date: " + date);
        }
        return synopticMeasurement;
    }

    @Override
    public SynopticMeasurement getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException {
        LocalDateTime saveDate = LocalDateTime.parse(date+ " 00:00:00", dateTimeFormatter);
        SynopticMeasurement synopticMeasurement;
        if (isValidDate(date)) {
            try {
                synopticMeasurement = synopticRepository.findColestPlacesByDate(saveDate, saveDate.plusDays(1));
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
        } else {
            throw new DateTimeException("Wrong date format!");
        }
        if (!Optional.ofNullable(synopticMeasurement).isPresent()) {
            throw new NoSuchElementException("Cant't find any synoptic measurements for date: " + date);
        }
        return synopticMeasurement;
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

    private boolean isValidNoOfResults(String noOfResults) {
        if(noOfResults.isEmpty() || !noOfResults.matches("\\d+")) {
         return false;
        } else {
            return true;
        }
    }

    private boolean isMeasurementLevelValid(AirMeasurementLevel airMeasurementLevel) {
        return Arrays.asList(AirMeasurementLevel.values()).contains(airMeasurementLevel);
    }
}
