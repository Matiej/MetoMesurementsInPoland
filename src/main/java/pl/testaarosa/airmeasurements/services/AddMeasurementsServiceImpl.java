package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationDetailsMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.mapper.SynopticMeasurementMapper;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
public class AddMeasurementsServiceImpl implements AddMeasurementsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AddMeasurementsServiceImpl.class);
    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationRepository measuringStationRepository;
    private final SynopticMeasurementRepository synopticRepository;
    private final AirMeasurementRepository airRepository;
    private final EmailNotifierService emailNotifierService;
    @Autowired
    private AddMeasurementRaportGenerator raportGenerator;

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository,
                                      SynopticMeasurementRepository synopticRepository, AirMeasurementRepository airRepository,
                                      EmailNotifierService emailNotifierService) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.emailNotifierService = emailNotifierService;
    }


    @Transactional
    @Override
    public MeasuringStation addOneStationMeasurement(Integer stationId) throws NumberFormatException, RestClientException,
            HibernateException, NoSuchElementException {
        long startTime1 = System.currentTimeMillis();
        AtomicReference<MeasuringStation> measuringStation = new AtomicReference<>();
        Map<String, SynopticMeasurement> synopticMeasurementsMap = new HashMap<>();
        if (!Optional.ofNullable(stationId).isPresent() && stationId.toString().matches("^[0-9]*$")) {
            LOGGER.error("StationID -> " + stationId + " is empty or format is incorrect!");
            throw new NumberFormatException("StationID -> " + stationId + " is empty or format is incorrect!");
        }
        //TODO uprościć. Mapę Station, SynopticMeasurement a nie String
        saveAllStations();
        if (!isStationIdInDb(stationId)) {
            throw new NoSuchElementException(ANSI_RED + "Can't find station id: " + stationId + " in data base!" + ANSI_RESET);
        }
        synopticMeasurementsMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        measuringStationRepository.findAll().stream().filter(m -> m.getStationId() == stationId).forEach(station -> {
            AirMeasurement airMeasurement = apiSupplierRetriever.airMeasurementProcessorById(station.getStationId());
            try {
                saveMeasuremets(synopticMeasurementsMap, station, airMeasurement);
                measuringStation.set(measuringStationRepository.save(station));
            } catch (HibernateException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
        });

        String timeer = timeer(System.currentTimeMillis() - startTime1);
        LOGGER.info("Measurement execution time: " + timeer);
        return measuringStation.get();
    }

    @Transactional
    @Override
    public List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException {
        long startTime1 = System.currentTimeMillis();
        List<MeasuringStation> mSList = new ArrayList<>();
        Map<String, SynopticMeasurement> synopticMeasurementsDtoMap = new HashMap<>();
        saveAllStations();
        //TODO moze po prostu zamiast new -> wydajność?
        synopticMeasurementsDtoMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        AtomicInteger counterAir = new AtomicInteger();
        AtomicInteger counterSynoptic = new AtomicInteger();
        AtomicInteger counterMeas = new AtomicInteger();
        measuringStationRepository.findAll().forEach(measuringStation -> {
            AirMeasurement airMeasurement = apiSupplierRetriever.airMeasurementProcessorById(measuringStation.getStationId());
            try {
                int[] counter = saveMeasuremets(synopticMeasurementsDtoMap, measuringStation, airMeasurement);
                counterAir.getAndAdd(counter[0]);
                counterSynoptic.getAndAdd(counter[1]);
                mSList.add(measuringStationRepository.save(measuringStation));
            } catch (HibernateException e) {
                e.printStackTrace();
                throw new RuntimeException("There is some db problem: " + e.getMessage());
            }
            counterMeas.getAndIncrement();
        });
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        String[] shortMess = {counterAir.toString(), counterMeas.toString(), counterSynoptic.toString(), timeer};
        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
        raportGenerator.createXMLReport(mSList);
        LOGGER.info("SAVED TOTAL MESUREMENTS FOR STATIONS-> " + counterMeas + " \nAIRMEASUREMENTS ->" + counterAir +
                " \nSYNOPTIC MEASUREMENTS-> " + counterSynoptic + " \n TOTAL TIME: " + timeer);
        return mSList;
    }

    @Transactional
    private int[] saveMeasuremets(Map<String, SynopticMeasurement> synopticMeasurementsDtoMap,
                                  MeasuringStation measuringStation, AirMeasurement airMeasurement) {
        int[] counters = new int[2];
        if (airMeasurement.getForeignId() == measuringStation.getStationId()) {
            airMeasurement.setMeasuringStation(measuringStation);
            airRepository.save(airMeasurement);
//            measuringStation.getAirMeasurementList().add(airMeasurement);
            LOGGER.info(ANSI_WHITE + "SAVED AIR MEASUREMENT FOR STATION ID -> " + measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
            counters[0]++;
        }
        if (synopticMeasurementsDtoMap.keySet().contains(measuringStation.getCity())) {
            SynopticMeasurement synopticMeasurement = synopticMeasurementsDtoMap.get(measuringStation.getCity());
            synopticMeasurement.setMeasuringStation(measuringStation);
//            measuringStation.getSynopticMeasurements().add(synopticMeasurement);
            synopticRepository.save(synopticMeasurement);
            LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
            counters[1]++;
        }
        return counters;
    }

    @Transactional
    //TODO poprawić moze fora zlikwidować jaoś czy
    private List<MeasuringStation> saveAllStations() throws HibernateException {
        List<MeasuringStation> measuringStationList = new LinkedList<>();
        for (MeasuringStation measuringStation : apiSupplierRetriever.measuringStationApiProcessor()) {
            measuringStationList.add(measuringStation);
            int id = measuringStation.getStationId();
            if (!measuringStationRepository.existsAllByStationId(id)) {
                try {
                    MeasuringStationDetails stDetails = measuringStation.getStationDetails();
                    measuringStation.setStationDetails(stDetails);
                    measuringStationRepository.save(measuringStation);
                } catch (HibernateException e) {
                    throw new RuntimeException("External data base server ERROR! Cant' add any measuring station to data base " + e.getMessage());
                }
            }
        }
        return measuringStationList;
    }

    private String timeer(Long timeMiliseconds) {
        DecimalFormat df2 = new DecimalFormat("###.###");
        return df2.format(timeMiliseconds / 60000.0);
    }

    private boolean isStationIdInDb(Integer stationId) {
        return measuringStationRepository.findAll().stream().anyMatch(m -> m.getStationId() == stationId);
    }
}
