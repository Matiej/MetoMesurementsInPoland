package pl.testaarosa.airmeasurements.services;

import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.mapper.AirMeasurementMapper;
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

import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_PURPLE;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RED;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RESET;

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

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository,
                                      AirMeasurementMapper airMapper, SynopticMeasurementMapper synopticMapper,
                                      SynopticMeasurementRepository synopticRepository,
                                      AirMeasurementRepository airRepository,
                                      MeasuringOnlineServices measuringOnlineServices, EmailNotifierService emailNotifierService) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.airMapper = airMapper;
        this.synopticMapper = synopticMapper;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.measuringOnlineServices = measuringOnlineServices;
        this.emailNotifierService = emailNotifierService;
    }

    public MeasuringStation addOne(Integer stationId) {
        long startTime1 = System.currentTimeMillis();
        Map<String, SynopticMeasurementDto> synopticMeasurementsDtoMap = new HashMap<>();
        AtomicReference<MeasuringStation> measuringStation = new AtomicReference<MeasuringStation>();
        try {
            measuringOnlineServices.addAllStations();
            synopticMeasurementsDtoMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!isStationId(stationId)) {
            throw new NoSuchElementException(ANSI_RED + "Can't find station id: !" + stationId + ANSI_RESET);
        }
        measuringStationRepository.findAll().stream().parallel().filter(m -> m.getStationId() == stationId).forEach(t -> {
            try {
                MeasuringStation t1 = t;
                CompletableFuture<AirMeasurementsDto> airMeasurementsDtoCompletableFuture = null;
                airMeasurementsDtoCompletableFuture = apiSupplierRetriever.airMeasurementsProcessorNew(t1.getStationId());

                CompletableFuture.allOf(airMeasurementsDtoCompletableFuture).join();
                AirMeasurements airMeasurements = null;
                airMeasurements = airMapper.mapToAirMeasurements(airMeasurementsDtoCompletableFuture.get());
                if (airMeasurements.getForeignId() == t.getStationId()) {
                    airMeasurements.setMeasuringStation(t1);
                    airRepository.save(airMeasurements);
                    t1.getAirMeasurementsList().add(airMeasurements);
                }
                if (synopticMeasurementsDtoMap.keySet().contains(t1.getCity())) {
                    SynopticMeasurements synopticMeasurements = synopticMapper.maptToSynopticMeasurement(synopticMeasurementsDtoMap.get(t.getCity()));
                    synopticMeasurements.setMeasuringStation(t1);
                    synopticRepository.save(synopticMeasurements);
                    LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + t1.getStationId() + " IN THE CITY -> " + t1.getCity() + ANSI_RESET);
                    t1.getSynopticMeasurements().add(synopticMeasurements);
                }
                measuringStation.set(measuringStationRepository.save(t1));
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        LOGGER.info("Measurement execution time: " + timeer);
        return measuringStation.get();
    }

    private boolean isStationId(Integer stationId) {
        return measuringStationRepository.findAll().stream().anyMatch(m -> m.getStationId() == stationId);
    }

    private SynopticMeasurementDto emptyObj() {
        return new SynopticMeasurementDto(9999, "->>no data available<<-", 9999.0, 9999.0, 9999.0, 9999.0);
    }

    @Transactional
    @Override
    public List<MeasuringStation> addMeasurementsAllStations() {
        long startTime1 = System.currentTimeMillis();
        Map<String, SynopticMeasurementDto> synopticMeasurementsDtoMap = new HashMap<>();
        List<MeasuringStation> mSList = new ArrayList<>();
        try {
            measuringOnlineServices.addAllStations();
            synopticMeasurementsDtoMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor().get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                counterMeas.getAndIncrement();
            } catch (InterruptedException e) {
                LOGGER.error(ANSI_RED + "Error, cant add all measurements!" + ANSI_RESET);
                e.printStackTrace();
            } catch (ExecutionException e) {
                LOGGER.error(ANSI_RED + "Error, cant add all measurements!" + ANSI_RESET);
                e.printStackTrace();
            }
        });
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        String[] shortMess = {counterAir.toString(), counterMeas.toString(), counterSynoptic.toString(), timeer};
        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
        LOGGER.info("SAVED TOTAL MESUREMENTS FOR STATIONS-> " + counterMeas + " \nAIRMEASUREMENTS ->" + counterAir +
                " \nSYNOPTIC MEASUREMENTS-> " + counterSynoptic + " \n TOTAL TIME: " + timeer);
        return mSList;
    }

    private String timeer(Long timeMiliseconds) {
        DecimalFormat df2 = new DecimalFormat("###.###");
        return df2.format(timeMiliseconds / 60000.0);
    }
}
