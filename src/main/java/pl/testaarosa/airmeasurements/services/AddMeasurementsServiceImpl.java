package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
public class AddMeasurementsServiceImpl implements AddMeasurementsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AddMeasurementsServiceImpl.class);
    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationRepository measuringStationRepository;
    private final SynopticMeasurementRepository synopticRepository;
    private final AirMeasurementRepository airRepository;
    private final EmailNotifierService emailNotifierService;
    private final AddMeasurementRaportGenerator raportGenerator;

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository,
                                      SynopticMeasurementRepository synopticRepository, AirMeasurementRepository airRepository,
                                      EmailNotifierService emailNotifierService, AddMeasurementRaportGenerator raportGenerator) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.emailNotifierService = emailNotifierService;
        this.raportGenerator = raportGenerator;
    }

    @Transactional
    @Override
    public MeasuringStation addOneStationMeasurement(Integer stationId) throws NumberFormatException, RestClientException,
            HibernateException, NoSuchElementException {
        long startTime1 = System.currentTimeMillis();

        if (!Optional.ofNullable(stationId).isPresent() || !stationId.toString().matches("^[0-9]*$")) {
            LOGGER.error("StationID -> " + stationId + " is empty or format is incorrect!");
            throw new NumberFormatException("StationID -> " + stationId + " is empty or format is incorrect!");
        }
        Map<String, SynopticMeasurement> synopticMeasurementMap = new HashMap<>();
        Map<MeasuringStation, AirMeasurement> mStResponseMap = new HashMap<>();
        try {
            mStResponseMap.putAll(saveStations(apiSupplierRetriever.airMeasurementsAndStProcessor(stationId)));
            synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        } catch (RestClientResponseException e) {
            throw new RestClientException("Can't find any synoptic measurement  because of REST API error-> " + e.getMessage());
        }
        int[] counter = saveMeasuremet(synopticMeasurementMap, mStResponseMap);

        String timeer = timeer(System.currentTimeMillis() - startTime1);
        LOGGER.info("SAVED TOTAL MEASUREMENTS FOR STATION ID: " + stationId + "\nAIR MEASUREMENTS ->" + counter[0] +
                " \nSYNOPTIC MEASUREMENTS-> " + counter[1] + " \n TOTAL TIME: " + timeer);
        return new ArrayList<>(mStResponseMap.keySet()).get(0);
    }

    @Transactional
    @Override
    public List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException {
        long startTime1 = System.currentTimeMillis();
        List<MeasuringStation> mSList = new ArrayList<>();
        Map<String, SynopticMeasurement> synopticMeasurementMap = new HashMap<>();
        Map<MeasuringStation, AirMeasurement> mStResponseMap = new HashMap<>();
        try {
            mStResponseMap.putAll(saveStations(apiSupplierRetriever.airMeasurementsAndStProcessor()));
            synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        } catch (RestClientResponseException e) {
            throw new RestClientException("Can't find any synoptic measurement  because of REST API error-> " + e.getMessage());
        }
        AtomicInteger counterAir = new AtomicInteger();
        AtomicInteger counterSynoptic = new AtomicInteger();
        AtomicInteger counterMeas = new AtomicInteger();

        int[] counter = saveMeasuremet(synopticMeasurementMap, mStResponseMap);
        counterAir.getAndAdd(counter[0]);
        counterSynoptic.getAndAdd(counter[1]);
        mSList.addAll(mStResponseMap.keySet());
        counterMeas.getAndIncrement();

        String timeer = timeer(System.currentTimeMillis() - startTime1);
        String[] shortMess = {counterAir.toString(), counterMeas.toString(), counterSynoptic.toString(), timeer};
        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
        raportGenerator.createXMLReport(mSList);
        LOGGER.info("SAVED TOTAL MESUREMENTS FOR STATIONS-> " + counterMeas + " \nAIR MEASUREMENTS ->" + counterAir +
                " \nSYNOPTIC MEASUREMENTS-> " + counterSynoptic + " \n TOTAL TIME: " + timeer);
        return mSList;
    }

    @Transactional
    private int[] saveMeasuremet(Map<String, SynopticMeasurement> synMstMap,
                                 Map<MeasuringStation, AirMeasurement> mStMap) throws HibernateException {
        int[] counters = new int[2];
        mStMap.entrySet().forEach(mSt -> {
            AirMeasurement airMeasurement = mSt.getValue();
            MeasuringStation measuringStation = mSt.getKey();
            try {
                airMeasurement.setMeasuringStation(measuringStation);
                airRepository.save(airMeasurement);
                LOGGER.info(ANSI_WHITE + "SAVED AIR MEASUREMENT FOR STATION ID -> " + measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                counters[0]++;
                if (synMstMap.keySet().contains(measuringStation.getCity())) {
                    SynopticMeasurement synopticMeasurement = synMstMap.get(measuringStation.getCity());
                    synopticMeasurement.setMeasuringStation(measuringStation);
                    synopticRepository.save(synopticMeasurement);
                    LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                    counters[1]++;
                }
                measuringStationRepository.save(measuringStation);
            } catch (RuntimeException e) {
                e.printStackTrace();
                throw new HibernateException("Can't save stationID: " + measuringStation.getStationId()
                        + " because of data base error-> " + e.getMessage());
            }
        });
        return counters;
    }

    @Transactional
    private Map<MeasuringStation, AirMeasurement> saveStations(Map<MeasuringStation, AirMeasurement> mStMap) throws HibernateException {
        Map<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
        try {
            mStMap.entrySet()
                    .forEach(st -> {
                        if (!measuringStationRepository.existsAllByStationId(st.getKey().getStationId())) {
                            measurementMap.put(measuringStationRepository.save(st.getKey()), st.getValue());
                        } else {
                            MeasuringStation byStationId = measuringStationRepository.findByStationId(st.getKey().getStationId());
                            measurementMap.put(byStationId, st.getValue());
                        }
                    });
            return measurementMap;
        } catch (HibernateException e) {
            throw new RuntimeException("External data base server ERROR! Cant' add any measuring station to data base " + e.getMessage());

        }
    }

    private String timeer(Long timeMiliseconds) {
        DecimalFormat df2 = new DecimalFormat("###.###");
        return df2.format(timeMiliseconds / 60000.0);
    }
//    @Transactional
//    private List<MeasuringStation> saveStations() throws HibernateException {
//        Map<MeasuringStation, AirMeasurement> measurementMap = apiSupplierRetriever.airMeasurementsAndStProcessor();
//        try {
//            List<MeasuringStation> measuringStations = new ArrayList<>();
//            measurementMap.keySet().stream()
//                    .forEach(st -> {
//                        if (!measuringStationRepository.existsAllByStationId(st.getStationId())) {
//                            measuringStationRepository.save(st);
//                            measuringStations.add(st);
//                        }
//                    });
//            return measuringStations;
//        } catch (HibernateException e) {
//            throw new RuntimeException("External data base server ERROR! Cant' add any measuring station to data base " + e.getMessage());
//
//        }
//    }


//    @Override
//    public List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException {
//        long startTime1 = System.currentTimeMillis();
//        List<MeasuringStation> mSList = new ArrayList<>();
//        Map<String, SynopticMeasurement> synopticMeasurementMap = new HashMap<>();
//        saveStations().v;
//        synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
//        AtomicInteger counterAir = new AtomicInteger();
//        AtomicInteger counterSynoptic = new AtomicInteger();
//        AtomicInteger counterMeas = new AtomicInteger();
//        measuringStationRepository.findAll().forEach(measuringStation -> {
//            try {
//                AirMeasurement airMeasurement = apiSupplierRetriever.airMeasurementProcessorById(measuringStation.getStationId());
//                try {
//                    int[] counter = saveMeasuremets(synopticMeasurementMap, measuringStation, airMeasurement);
//                    counterAir.getAndAdd(counter[0]);
//                    counterSynoptic.getAndAdd(counter[1]);
//                    mSList.add(measuringStationRepository.save(measuringStation));
//                } catch (HibernateException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException("There is some db problem: " + e.getMessage());
//                }
//                counterMeas.getAndIncrement();
//            } catch (RestClientResponseException e) {
//                throw new RestClientException("Can't find any air measurement for stationID: " + measuringStation.getStationId() + " because of REST API error-> " + e.getMessage());
//            }
//        });
//        String timeer = timeer(System.currentTimeMillis() - startTime1);
//        String[] shortMess = {counterAir.toString(), counterMeas.toString(), counterSynoptic.toString(), timeer};
//        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
//        raportGenerator.createXMLReport(mSList);
//        LOGGER.info("SAVED TOTAL MESUREMENTS FOR STATIONS-> " + counterMeas + " \nAIRMEASUREMENTS ->" + counterAir +
//                " \nSYNOPTIC MEASUREMENTS-> " + counterSynoptic + " \n TOTAL TIME: " + timeer);
//        return mSList;
//    }


//
//    @Transactional
//    @Override
//    public List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException {
//        long startTime1 = System.currentTimeMillis();
//        List<MeasuringStation> mSList = new ArrayList<>();
//        Map<String, SynopticMeasurement> synopticMeasurementMap = new HashMap<>();
//        Map<MeasuringStation, AirMeasurement> measurementMap = saveStations();
//        synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
//
//        AtomicInteger counterAir = new AtomicInteger();
//        AtomicInteger counterSynoptic = new AtomicInteger();
//        AtomicInteger counterMeas = new AtomicInteger();
//        measuringStationRepository.findAll().forEach(measuringStation -> {
//            try {
//                AirMeasurement airMeasurement = apiSupplierRetriever.airMeasurementProcessorById(measuringStation.getStationId());
//                try {
//                    int[] counter = saveMeasuremets(synopticMeasurementMap, measuringStation, airMeasurement);
//                    counterAir.getAndAdd(counter[0]);
//                    counterSynoptic.getAndAdd(counter[1]);
//                    mSList.add(measuringStationRepository.save(measuringStation));
//                } catch (HibernateException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException("There is some db problem: " + e.getMessage());
//                }
//                counterMeas.getAndIncrement();
//            } catch (RestClientResponseException e) {
//                throw new RestClientException("Can't find any air measurement for stationID: " + measuringStation.getStationId() + " because of REST API error-> " + e.getMessage());
//            }
//        });
//        String timeer = timeer(System.currentTimeMillis() - startTime1);
//        String[] shortMess = {counterAir.toString(), counterMeas.toString(), counterSynoptic.toString(), timeer};
//        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
//        raportGenerator.createXMLReport(mSList);
//        LOGGER.info("SAVED TOTAL MESUREMENTS FOR STATIONS-> " + counterMeas + " \nAIRMEASUREMENTS ->" + counterAir +
//                " \nSYNOPTIC MEASUREMENTS-> " + counterSynoptic + " \n TOTAL TIME: " + timeer);
//        return mSList;
//    }

//    @Transactional
//    @Override
//    public MeasuringStation addOneStationMeasurement(Integer stationId) throws NumberFormatException, RestClientException,
//            HibernateException, NoSuchElementException {
//        long startTime1 = System.currentTimeMillis();
//        AtomicReference<MeasuringStation> measuringStation = new AtomicReference<>();
//        Map<String, SynopticMeasurement> synopticMeasurementsMap = new HashMap<>();
//        if (!Optional.ofNullable(stationId).isPresent() || !stationId.toString().matches("^[0-9]*$")) {
//            LOGGER.error("StationID -> " + stationId + " is empty or format is incorrect!");
//            throw new NumberFormatException("StationID -> " + stationId + " is empty or format is incorrect!");
//        }
////        saveAllStations();
//        saveStations();
//        if (!isStationIdInDb(stationId)) {
//            throw new NoSuchElementException(ANSI_RED + "Can't find station id: " + stationId + " in data base!" + ANSI_RESET);
//        }
//        try {
//            synopticMeasurementsMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
//            measuringStationRepository.findAll().stream().filter(m -> m.getStationId() == stationId).forEach(station -> {
//                AirMeasurement airMeasurement = apiSupplierRetriever.airMeasurementProcessorById(station.getStationId());
//                try {
//                    saveMeasuremets(synopticMeasurementsMap, station, airMeasurement);
//                    measuringStation.set(measuringStationRepository.save(station));
//                } catch (HibernateException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException("There is some db problem: " + e.getMessage());
//                }
//            });
//        } catch (RestClientResponseException e) {
//            throw new RestClientException("Can't find any air measurement for stationID: " + stationId + " because of REST API error-> " + e.getMessage());
//
//        }
//        String timeer = timeer(System.currentTimeMillis() - startTime1);
//        LOGGER.info("Measurement execution time: " + timeer);
//        return measuringStation.get();
//    }


}
