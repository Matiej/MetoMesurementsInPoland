package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationDetailsMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.mapper.SynopticMeasurementMapper;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

//TODO refaktor konkret!.
@Service
public class AddMeasurementsServiceImpl implements AddMeasurementsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AddMeasurementsServiceImpl.class);
    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationRepository measuringStationRepository;
    private final AirMeasurementMapper airMapper;
    private final SynopticMeasurementMapper synopticMapper;
    private final SynopticMeasurementRepository synopticRepository;
    private final AirMeasurementRepository airRepository;
    private final MeasuringOnlineServices measuringOnlineServices;
    private final EmailNotifierService emailNotifierService;
    private final MeasuringStationMapper stMapper;
    private final MeasuringStationDetailsMapper staDetMapper;

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository,
                                      AirMeasurementMapper airMapper, SynopticMeasurementMapper synopticMapper,
                                      SynopticMeasurementRepository synopticRepository,
                                      AirMeasurementRepository airRepository,
                                      MeasuringOnlineServices measuringOnlineServices, EmailNotifierService emailNotifierService,
                                      MeasuringStationMapper stMapper, MeasuringStationDetailsMapper staDetMapper) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.airMapper = airMapper;
        this.synopticMapper = synopticMapper;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.measuringOnlineServices = measuringOnlineServices;
        this.emailNotifierService = emailNotifierService;
        this.stMapper = stMapper;
        this.staDetMapper = staDetMapper;
    }

//    @Transactional
//    @Override
//    public MeasuringStation addOne(Integer stationId) throws RestClientException, HibernateException {
//        long startTime1 = System.currentTimeMillis();
//        AtomicReference<MeasuringStation> measuringStation = new AtomicReference<MeasuringStation>();
//        Map<String, SynopticMeasurementDto> synopticMeasurementsDtoMap = new HashMap<>();
//        addAllStations();
//        synopticMeasurementsDtoMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor().get());
//
//        if (!isStationIdInDb(stationId)) {
//            throw new NoSuchElementException(ANSI_RED + "Can't find station id: !" + stationId + ANSI_RESET);
//        }
//        measuringStationRepository.findAll().stream().parallel().filter(m -> m.getStationId() == stationId).forEach(t -> {
//
//            MeasuringStation t1 = t;
//            CompletableFuture<AirMeasurementsDto> airMeasurementsDtoCompletableFuture = apiSupplierRetriever.airMeasurementsProcessorNew(t1.getStationId());
//
//            CompletableFuture.allOf(airMeasurementsDtoCompletableFuture).join();
//            AirMeasurements airMeasurements = airMapper.mapToAirMeasurements(airMeasurementsDtoCompletableFuture.get());
//            try {
//                if (airMeasurements.getForeignId() == t.getStationId()) {
//                    airMeasurements.setMeasuringStation(t1);
//                    airRepository.save(airMeasurements);
//                    t1.getAirMeasurementsList().add(airMeasurements);
//                }
//                if (synopticMeasurementsDtoMap.keySet().contains(t1.getCity())) {
//                    SynopticMeasurements synopticMeasurements = synopticMapper.maptToSynopticMeasurement(synopticMeasurementsDtoMap.get(t.getCity()));
//                    synopticMeasurements.setMeasuringStation(t1);
//                    synopticRepository.save(synopticMeasurements);
//                    LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + t1.getStationId() + " IN THE CITY -> " + t1.getCity() + ANSI_RESET);
//                    t1.getSynopticMeasurements().add(synopticMeasurements);
//                }
//                measuringStation.set(measuringStationRepository.save(t1));
//            } catch (HibernateException e) {
//                e.printStackTrace();
//                throw new RuntimeException("There is some db problem: " + e.getMessage());
//            }
//        });
//        String timeer = timeer(System.currentTimeMillis() - startTime1);
//        LOGGER.info("Measurement execution time: " + timeer);
//        return measuringStation.get();
//    }
    @Transactional
    @Override
    public MeasuringStation addOne(Integer stationId) throws IllegalArgumentException,RestClientException,
            HibernateException, NoSuchElementException {
        long startTime1 = System.currentTimeMillis();
        AtomicReference<MeasuringStation> measuringStation = new AtomicReference<>();
        Map<String, SynopticMeasurementDto> synopticMeasurementsDtoMap = new HashMap<>();
        if(!Optional.ofNullable(stationId).isPresent() && stationId.toString().matches("^[0-9]*$")) {
            LOGGER.error("StationID -> " + stationId +" is empty or format is incorrect!");
            throw new IllegalArgumentException("StationID -> " + stationId +" is empty or format is incorrect!");
        }
        try {
            addAllStations();
            if (!isStationIdInDb(stationId)) {
                throw new NoSuchElementException(ANSI_RED + "Can't find station id: " + stationId + " in data base!"  + ANSI_RESET);
            }
            synopticMeasurementsDtoMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor().get());

            measuringStationRepository.findAll().stream().filter(m -> m.getStationId() == stationId).forEach(t -> {
                CompletableFuture<AirMeasurementsDto> airMeasurementsDtoCompletableFuture = apiSupplierRetriever.airMeasurementsProcessorNew(t.getStationId());

                CompletableFuture.allOf(airMeasurementsDtoCompletableFuture).join();
                AirMeasurements airMeasurements;
                try {
                    airMeasurements = airMapper.mapToAirMeasurements(airMeasurementsDtoCompletableFuture.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    throw new RestClientException("Can't add measurements for station-> " + stationId + " because of REST API error" + e.getMessage());
                }
                try {
                    if (airMeasurements.getForeignId() == t.getStationId()) {
                        airMeasurements.setMeasuringStation(t);
                        airRepository.save(airMeasurements);
                        t.getAirMeasurementsList().add(airMeasurements);
                    }
                    if (synopticMeasurementsDtoMap.keySet().contains(t.getCity())) {
                        SynopticMeasurements synopticMeasurements = synopticMapper.maptToSynopticMeasurement(synopticMeasurementsDtoMap.get(t.getCity()));
                        synopticMeasurements.setMeasuringStation(t);
                        t.getSynopticMeasurements().add(synopticMeasurements);
                        synopticRepository.save(synopticMeasurements);
                        LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + t.getStationId() + " IN THE CITY -> " + t.getCity() + ANSI_RESET);
                    }
                    measuringStation.set(measuringStationRepository.save(t));
                } catch (HibernateException e) {
                    e.printStackTrace();
                    throw new RuntimeException("There is some db problem: " + e.getMessage());
                }
            });
        } catch (ExecutionException|InterruptedException e) {
            throw new RestClientException("Can't add measurements for station-> " + stationId + " because of REST API error" + e.getMessage());
        }
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        LOGGER.info("Measurement execution time: " + timeer);
        return measuringStation.get();
    }

    @Transactional
    @Override
    public List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException {
        long startTime1 = System.currentTimeMillis();
        List<MeasuringStation> mSList = new ArrayList<>();
        Map<String, SynopticMeasurementDto> synopticMeasurementsDtoMap = new HashMap<>();
        try {
            addAllStations();
            synopticMeasurementsDtoMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor().get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException("Can't find any measuring stations because of REST API error-> " + e.getMessage());
        }
        AtomicInteger counterAir = new AtomicInteger();
        AtomicInteger counterMeas = new AtomicInteger();
        AtomicInteger counterSynoptic = new AtomicInteger();
        measuringStationRepository.findAll().forEach(t -> {
            try {
                MeasuringStation t1 = t;
                CompletableFuture<AirMeasurementsDto> airMeasurementsDtoCompletableFuture = apiSupplierRetriever.airMeasurementsProcessorNew(t1.getStationId());
                CompletableFuture.allOf(airMeasurementsDtoCompletableFuture).join();
                AirMeasurements airMeasurements = airMapper.mapToAirMeasurements(airMeasurementsDtoCompletableFuture.get());
                try {
                    if (airMeasurements.getForeignId() == t.getStationId()) {
                        airMeasurements.setMeasuringStation(t1);
                        airRepository.save(airMeasurements);
                        t1.getAirMeasurementsList().add(airMeasurements);
                        counterAir.getAndIncrement();
                    }
                    if (synopticMeasurementsDtoMap.keySet().contains(t1.getCity())) {
                        SynopticMeasurements synopticMeasurements = synopticMapper.maptToSynopticMeasurement(synopticMeasurementsDtoMap.get(t.getCity()));
                        synopticMeasurements.setMeasuringStation(t1);
                        synopticRepository.save(synopticMeasurements);
                        LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + t1.getStationId() + " IN THE CITY -> " + t1.getCity() + ANSI_RESET);
                        t1.getSynopticMeasurements().add(synopticMeasurements);
                        counterSynoptic.getAndIncrement();
                    }
                    mSList.add(measuringStationRepository.save(t1));
                } catch (HibernateException e) {
                    e.printStackTrace();
                    throw new RuntimeException("There is some db problem: " + e.getMessage());
                }
                counterMeas.getAndIncrement();
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error(ANSI_RED + "Error, cant add all measurements!" + ANSI_RESET);
                e.printStackTrace();
                throw new RestClientException("Can't find any measuring stations because of REST API error-> " + e.getMessage());
            }
        });
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        String[] shortMess = {counterAir.toString(), counterMeas.toString(), counterSynoptic.toString(), timeer};
        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
        LOGGER.info("SAVED TOTAL MESUREMENTS FOR STATIONS-> " + counterMeas + " \nAIRMEASUREMENTS ->" + counterAir +
                " \nSYNOPTIC MEASUREMENTS-> " + counterSynoptic + " \n TOTAL TIME: " + timeer);
        return mSList;
    }

    private boolean isStationIdInDb(Integer stationId) {
        return measuringStationRepository.findAll().stream().anyMatch(m -> m.getStationId() == stationId);
    }

    @Transactional
    //TODO samo zło. Poprawic tą metodę i przenieść do innej klasy czy cos -> masakra
    private List<MeasuringStation> addAllStations() throws RestClientException {
        List<MeasuringStation> measuringStationList = new LinkedList<>();
        try {
            for (MeasuringStationDto measuringStationDto : apiSupplierRetriever.measuringStationApiProcessor().get()) {
                MeasuringStation measuringStation = stMapper.mapToMeasuringSt(measuringStationDto);
                measuringStationList.add(measuringStation);
                int id = measuringStationDto.getId();
                if (!measuringStationRepository.existsAllByStationId(id)) {
                    MeasuringStationDetails stDetails = staDetMapper.mapToStationDetails(measuringStationDto);
                    measuringStation.setStationDetails(stDetails);
                    measuringStationRepository.save(measuringStation);
                }
            }
            return measuringStationList;
        } catch (InterruptedException | ExecutionException e) {
            throw new RestClientException("Can't find any measuring stations because of REST API error-> " + e.getMessage());
        }
    }

    private String timeer(Long timeMiliseconds) {
        DecimalFormat df2 = new DecimalFormat("###.###");
        return df2.format(timeMiliseconds / 60000.0);
    }
}
