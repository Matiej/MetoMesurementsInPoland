package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetMeasurementsServiceImpl implements GetMeasurementsService {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MeasuringStationRepository stationRepository;
    private final AirMeasurementRepository airRepository;
    private final SynopticMeasurementRepository synopticRepository;

    @Autowired
    public GetMeasurementsServiceImpl(MeasuringStationRepository stationRepository, AirMeasurementRepository airRepository, SynopticMeasurementRepository synopticRepository) {
        this.stationRepository = stationRepository;
        this.airRepository = airRepository;
        this.synopticRepository = synopticRepository;
    }

    @Override
    public List<MeasuringStation> findAll() throws NoSuchElementException, HibernateException {
        try {
            List<MeasuringStation> measuringStationList = stationRepository.findAll();
            if (measuringStationList.isEmpty()) {
                throw new NoSuchElementException("Can't find any measurements in data base");
            } else {
                return measuringStationList;
            }
        } catch (HibernateException e) {
            throw new RuntimeException("There is some db problem: " + e.getMessage());
        }
    }

    @Override
    public List<AirMeasurements> getAirMeasurements(MeasurementsAirLevel measurementsAirLevel) throws IllegalArgumentException,
            NoSuchElementException, HibernateException {
        List<AirMeasurements> allByAirQuality;
        if (isMeasurementLevelValid(measurementsAirLevel)) {
            try {
                allByAirQuality = airRepository.findAllByAirQuality(measurementsAirLevel);
            } catch (HibernateException e) {
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Not recognized enum: " + measurementsAirLevel);
        }
        if (allByAirQuality.isEmpty()) {
            throw new NoSuchElementException("There are no measurements for given air level: " + measurementsAirLevel);
        }
        return allByAirQuality;
    }

    @Override
    public List<AirMeasurements> getAirMeasurements(String date) throws DateTimeException, NoSuchElementException, HibernateException {
        List<AirMeasurements> airMeasurementsList = new ArrayList<>();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                airMeasurementsList = airRepository.findAll()
                        .stream()
                        .filter(a -> a.getSaveDate().toLocalDate().isEqual(localDate))
                        .collect(Collectors.toList());
            } catch (HibernateException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db connection problem: " + e.getMessage());
            }
        } else if (!isValidDate(date)) {
            throw new DateTimeException("Wrong date format!");
        }
        if (airMeasurementsList.isEmpty()) {
            throw new NoSuchElementException("Cant't find any air measurements for date: " + date);
        }
        return airMeasurementsList;
    }

    @Override
    public List<SynopticMeasurements> getSynopticMeasuremets(String date) throws DateTimeException, NoSuchElementException,
            HibernateException {
        List<SynopticMeasurements> synopticMeasurementsList = new ArrayList<>();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                synopticMeasurementsList = synopticRepository.findAll().stream()
                        .filter(a -> a.getSaveDate().toLocalDate().isEqual(localDate))
                        .collect(Collectors.toList());
            } catch (HibernateException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db connection problem: " + e.getMessage());
            }
        } else if (!isValidDate(date)) {
            throw new DateTimeException("Wrong date format!");
        }
        if (synopticMeasurementsList.isEmpty()) {
            throw new NoSuchElementException("Cant't find any synoptic measurements for date: " + date);
        }
        return synopticMeasurementsList;
    }


    @Override
    public SynopticMeasurements getHottestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException,
            HibernateException {
        SynopticMeasurements synopticMeasurement = new SynopticMeasurements();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                synopticMeasurement = synopticRepository.findAll()
                        .stream()
                        .filter(a -> a.getTemperature() < 9999 && a.getSaveDate()
                                .toLocalDate()
                                .isEqual(localDate))
                        .max(Comparator.comparing(SynopticMeasurements::getTemperature)
                                .thenComparing(SynopticMeasurements::getAirHumidity)
                                .reversed()
                                .thenComparing(SynopticMeasurements::getWindSpeed)
                                .reversed())
                        .orElse(null);
            } catch (HibernateException e) {
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
    public SynopticMeasurements getColdestPlaceGivenDate(String date) throws DateTimeException, NoSuchElementException,
            HibernateException {
        SynopticMeasurements synopticColdestMeasurement = new SynopticMeasurements();
        if (isValidDate(date)) {
            try {
                LocalDate localDate = LocalDate.parse(date, formatter);
                synopticColdestMeasurement = synopticRepository.findAll().stream()
                        .filter(a -> a.getTemperature() < 9999 && a.getSaveDate()
                                .toLocalDate()
                                .isEqual(localDate))
                        .min(Comparator.comparing(SynopticMeasurements::getTemperature)
                                .thenComparing(SynopticMeasurements::getAirHumidity)
                                .reversed()
                                .thenComparing(SynopticMeasurements::getWindSpeed)
                                .reversed())
                        .orElse(null);
            } catch (HibernateException e) {
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

    @Override
    public List<SynopticMeasurements> getHottestPlaces() throws NoSuchElementException, HibernateException {
        List<SynopticMeasurements> measurementsList = new ArrayList<>();
        try {
            measurementsList = synopticRepository.findAll()
                    .stream()
                    .filter(a -> a.getTemperature() < 9999)
                    .sorted(Comparator.comparing(SynopticMeasurements::getTemperature)
                            .reversed()
                            .thenComparing(SynopticMeasurements::getAirHumidity)
                            .thenComparing(SynopticMeasurements::getWindSpeed))
                    .collect(Collectors.toList());
        } catch (HibernateException e) {
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
    public List<SynopticMeasurements> getColdestPlaces() throws NoSuchElementException, HibernateException {
        List<SynopticMeasurements> synopticMeasurementsList;
        try {
            synopticMeasurementsList = synopticRepository.findAll()
                    .stream()
                    .filter(a -> a.getTemperature() < 9999)
                    .sorted(Comparator.comparing(SynopticMeasurements::getTemperature)
                            .thenComparing(SynopticMeasurements::getAirHumidity)
                            .reversed()
                            .thenComparing(SynopticMeasurements::getWindSpeed)
                            .reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new RuntimeException("There is some db problem: " + e.getMessage());
        }
        if (synopticMeasurementsList.isEmpty()) {
            throw new NoSuchElementException("Can't find coldest 10 measurements");
        } else {
            return synopticMeasurementsList;
        }
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

    private boolean isMeasurementLevelValid(MeasurementsAirLevel measurementsAirLevel) {
        return Arrays.stream(MeasurementsAirLevel.values()).anyMatch(m -> m.equals(measurementsAirLevel));
    }
}

