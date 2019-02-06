package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoredMeasurementsServiceImpl implements StoredMeasurementsService {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MeasuringStationRepository stationRepository;
    private final AirMeasurementRepository airRepository;
    private final SynopticMeasurementRepository synopticRepository;

    @Autowired
    public StoredMeasurementsServiceImpl(MeasuringStationRepository stationRepository, AirMeasurementRepository airRepository, SynopticMeasurementRepository synopticRepository) {
        this.stationRepository = stationRepository;
        this.airRepository = airRepository;
        this.synopticRepository = synopticRepository;
    }

    @Override
    public List<MeasuringStation> findAll() throws NoSuchElementException, DataIntegrityViolationException {
        try {
            List<MeasuringStation> measuringStationList = stationRepository.findAll();
            if (measuringStationList.isEmpty()) {
                throw new NoSuchElementException("Can't find any measurements in data base");
            } else {
                return measuringStationList;
            }
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("There is some db problem: " + e.getMessage());
        }
    }

    @Override
    public List<SynopticMeasurement> getSynopticMeasuremets(String date) throws DateTimeException, NoSuchElementException,
            DataIntegrityViolationException {
        List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                synopticMeasurementList = synopticRepository.findAll().stream()
                        .filter(a -> a.getSaveDate().toLocalDate().isEqual(localDate))
                        .collect(Collectors.toList());
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
    public List<AirMeasurement> getAirMeasurementsByLevel(AirMeasurementLevel airMeasurementLevel) throws IllegalArgumentException,
            NoSuchElementException, DataIntegrityViolationException {
        List<AirMeasurement> allByAirQuality;
        if (isMeasurementLevelValid(airMeasurementLevel)) {
            try {
                allByAirQuality = airRepository.findAllByAirQuality(airMeasurementLevel);
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
    public List<AirMeasurement> getAirMeasurementsByDate(String date) throws DateTimeException, NoSuchElementException, DataIntegrityViolationException {
        List<AirMeasurement> airMeasurementList = new ArrayList<>();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                airMeasurementList = airRepository.findAll()
                        .stream()
                        .filter(a -> a.getSaveDate().toLocalDate().isEqual(localDate))
                        .collect(Collectors.toList());
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


    @Override
    public List<SynopticMeasurement> getHottestPlaces() throws NoSuchElementException, DataIntegrityViolationException {
        List<SynopticMeasurement> measurementsList = new ArrayList<>();
        try {
            measurementsList = synopticRepository.findAll()
                    .stream()
                    .filter(a -> a.getTemperature() < 9999)
                    .sorted(Comparator.comparing(SynopticMeasurement::getTemperature)
                            .reversed()
                            .thenComparing(SynopticMeasurement::getAirHumidity)
                            .thenComparing(SynopticMeasurement::getWindSpeed))
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new RuntimeException("There is some db problem: " + e.getMessage());
        }
        if (measurementsList.isEmpty()) {
            throw new NoSuchElementException("Can't find hottest 10 measurements");
        } else {
            return measurementsList;
        }
    }

    @Override
    public List<SynopticMeasurement> getColdestPlaces() throws NoSuchElementException, HibernateException {
        List<SynopticMeasurement> synopticMeasurementList;
        try {
            synopticMeasurementList = synopticRepository.findAll()
                    .stream()
                    .filter(a -> a.getTemperature() < 9999)
                    .sorted(Comparator.comparing(SynopticMeasurement::getTemperature)
                            .thenComparing(SynopticMeasurement::getAirHumidity)
                            .reversed()
                            .thenComparing(SynopticMeasurement::getWindSpeed)
                            .reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new RuntimeException("There is some db problem: " + e.getMessage());
        }
        if (synopticMeasurementList.isEmpty()) {
            throw new NoSuchElementException("Can't find coldest 10 measurements");
        } else {
            return synopticMeasurementList;
        }
    }

    @Override
    public SynopticMeasurement getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException,
            DataIntegrityViolationException {
        SynopticMeasurement synopticMeasurement = new SynopticMeasurement();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                synopticMeasurement = synopticRepository.findAll()
                        //TODO spradzić to filtrowanie czy aby potrzebne
                        .stream()
                        .filter(a -> a.getTemperature() < 9999 && a.getSaveDate()
                                .toLocalDate()
                                .isEqual(localDate))
                        .max(Comparator.comparing(SynopticMeasurement::getTemperature)
                                .thenComparing(SynopticMeasurement::getAirHumidity)
                                .reversed()
                                .thenComparing(SynopticMeasurement::getWindSpeed)
                                .reversed())
                        .orElse(null);
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
    public SynopticMeasurement getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException,
            DataIntegrityViolationException {
        SynopticMeasurement synopticColdestMeasurement = new SynopticMeasurement();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                synopticColdestMeasurement = synopticRepository.findAll().stream()
                        .filter(a -> a.getTemperature() < 9999 && a.getSaveDate()
                                .toLocalDate()
                                .isEqual(localDate))
                        .min(Comparator.comparing(SynopticMeasurement::getTemperature)
                                .thenComparing(SynopticMeasurement::getAirHumidity)
                                .reversed()
                                .thenComparing(SynopticMeasurement::getWindSpeed)
                                .reversed())
                        .orElse(null);
            } catch (DataIntegrityViolationException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
        } else if (!isValidDate(date)) {
            throw new DateTimeException("Wrong date format!");
        }
        if (!Optional.ofNullable(synopticColdestMeasurement).isPresent()) {
            throw new NoSuchElementException("Cant't find any synoptic measurements for date: " + date);
        }
        return synopticColdestMeasurement;
    }
//TODO poprawić upościc
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

