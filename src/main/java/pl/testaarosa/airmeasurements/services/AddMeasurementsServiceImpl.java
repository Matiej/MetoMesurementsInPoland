package pl.testaarosa.airmeasurements.services;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.AirMeasurementRepository;
import pl.testaarosa.airmeasurements.repositories.CityRepository;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.SynopticMeasurementRepository;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.util.*;

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
    private final CityRepository cityRepository;

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository,
                                      SynopticMeasurementRepository synopticRepository, AirMeasurementRepository airRepository,
                                      EmailNotifierService emailNotifierService, AddMeasurementRaportGenerator raportGenerator,
                                      CityRepository cityRepository) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.emailNotifierService = emailNotifierService;
        this.raportGenerator = raportGenerator;
        this.cityRepository = cityRepository;
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
            mStResponseMap.putAll(saveAirMeasurementSt(apiSupplierRetriever.airMeasurementsAndStProcessor(stationId)));
            synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        } catch (RestClientResponseException e) {
            throw new RestClientException("Can't find any synoptic measurement  because of REST API error-> " + e.getMessage());
        }
        int[] counter = saveSynMeasuremet(synopticMeasurementMap, mStResponseMap, false);

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
        LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap = new LinkedHashMap<>();
        LinkedHashMap<MeasuringStation, AirMeasurement> mStResponseMap = new LinkedHashMap<>();
        try {
            mStResponseMap.putAll(saveAirMeasurementSt(apiSupplierRetriever.airMeasurementsAndStProcessor()));
            synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        } catch (RestClientResponseException e) {
            throw new RestClientException("Can't find any synoptic measurement  because of REST API error-> " + e.getMessage());
        }
        int[] counter = saveSynMeasuremet(synopticMeasurementMap, mStResponseMap, true);
        mSList.addAll(mStResponseMap.keySet());
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        String[] shortMess = {String.valueOf(counter[0]), String.valueOf(counter[1]), String.valueOf(counter[1]), timeer};
        String report = emailNotifierService.sendEmailAfterDownloadMeasurementsN(mSList, shortMess);
        raportGenerator.createXMLReport(mSList);
        LOGGER.info("SAVED TOTAL MEASUREMENTS FOR => " + "\nAIR MEASUREMENTS ->" + counter[0] +
                " \nSYNOPTIC MEASUREMENTS-> " + counter[1] + " \n TOTAL TIME: " + timeer);
        return mSList;
    }

    @Transactional
    private Map<MeasuringStation, AirMeasurement> saveAirMeasurementSt(Map<MeasuringStation, AirMeasurement> mStMap) throws HibernateException {
        Map<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();

        try {
            mStMap.entrySet()
                    .forEach(st -> {
                        AirMeasurement airMeasurement = st.getValue();
                        MeasuringStation measuringStation = st.getKey();
                        City city = new City();
                        try {
                            if (!measuringStationRepository.existsAllByStationId(measuringStation.getStationId())) {
                                airMeasurement.setMeasuringStation(measuringStation);
                                airRepository.save(airMeasurement);
                                LOGGER.info(ANSI_WHITE + "SAVED AIR MEASUREMENT FOR STATION ID -> " +
                                        measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                                measurementMap.put(measuringStationRepository.save(measuringStation), airMeasurement);
                                LOGGER.info(ANSI_WHITE + "SAVED NEW MEASURING STATION ID -> " +
                                        measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                            } else {
                                MeasuringStation byStationId = measuringStationRepository.findByStationId(measuringStation.getStationId());
                                airMeasurement.setMeasuringStation(byStationId);
                                airRepository.save(airMeasurement);
                                measuringStationRepository.save(byStationId);
                                LOGGER.info(ANSI_GREEN + "SAVED AIR MEASUREMENT FOR STATION ID -> " +
                                        measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                                measurementMap.put(measuringStationRepository.save(byStationId), airMeasurement);
                            }
                            if (!cityRepository.existsAllByCityName(measuringStation.getCity())) {
                                city.setCityName(measuringStation.getCity());
                                city.getAirMeasurementList().add(airMeasurement);
                                airRepository.save(airMeasurement);
                                airMeasurement.getCityList().add(city);
                                cityRepository.save(city);
                                LOGGER.info("SAVE NEW CITY: " + city.getCityName());
                            } else {
                                City oneByCityName = cityRepository.findOneByCityName(measuringStation.getCity());
                                oneByCityName.getAirMeasurementList().add(airMeasurement);
                                airRepository.save(airMeasurement);
                                cityRepository.save(oneByCityName);
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            throw new HibernateException("Can't save stationID: " + measuringStation.getStationId()
                                    + " because of data base error-> " + e.getMessage());
                        }
                    });
            return measurementMap;
        } catch (HibernateException e) {
            throw new RuntimeException("External data base server ERROR! Cant' add any measuring station to data base " + e.getMessage());

        }
    }


    @Transactional
    private int[] saveSynMeasuremet(Map<String, SynopticMeasurement> synMstMap,
                                    Map<MeasuringStation, AirMeasurement> mStMap, boolean forAllMeasurements) throws HibernateException {
        int[] counters = new int[2];
        List<SynopticMeasurement> tmpSynList = new ArrayList<>();
        try {
            mStMap.forEach((measuringStation, value) -> {
                if (synMstMap.keySet().contains(measuringStation.getCity())) {
                    SynopticMeasurement synopticMeasurement = synMstMap.get(measuringStation.getCity());
                    City oneByCityName = cityRepository.findOneByCityName(measuringStation.getCity());
                    tmpSynList.add(synopticMeasurement);

                    synopticMeasurement.getMeasuringStation().add(measuringStation);
                    measuringStation.getSynopticMeasurements().add(synopticMeasurement);
                    synopticMeasurement.setCity(oneByCityName);
                    oneByCityName.getSynopticMeasurementList().add(synopticMeasurement);
                    synopticRepository.save(synopticMeasurement);
                    measuringStationRepository.save(measuringStation);
                    cityRepository.save(oneByCityName);
                    LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " +
                            measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                    counters[1]++;
                }
            });
            if (forAllMeasurements) {
                synMstMap.forEach((key, value) -> {
                    if (tmpSynList.stream().noneMatch(value::equals)) {
                        City city = new City();
                        city.setCityName(value.getCityName());
                        value.setCity(city);
                        synopticRepository.save(value);
                        cityRepository.save(city);
                        LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT THAT HAVE NO AIR STATION IN THE CITY -> "
                                + value.getCityName() + ANSI_RESET);
                    }
                });
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new HibernateException("Can't save synoptic measurements  because of data base error-> " + e.getMessage());
        }
        return counters;
    }

    private String timeer(Long timeMiliseconds) {
        DecimalFormat df2 = new DecimalFormat("###.###");
        return df2.format(timeMiliseconds / 60000.0);
    }

//    @Transactional
//    private Map<MeasuringStation, AirMeasurement> saveAirMeasurementSt(Map<MeasuringStation, AirMeasurement> mStMap) throws HibernateException {
//        Map<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
//        try {
//            mStMap.entrySet()
//                    .forEach(st -> {
//                        if (!measuringStationRepository.existsAllByStationId(st.getKey().getStationId())) {
//                            measurementMap.put(measuringStationRepository.save(st.getKey()), st.getValue());
//                        } else {
//                            MeasuringStation byStationId = measuringStationRepository.findByStationId(st.getKey().getStationId());
//                            measurementMap.put(byStationId, st.getValue());
//                        }
//                    });
//            return measurementMap;
//        } catch (HibernateException e) {
//            throw new RuntimeException("External data base server ERROR! Cant' add any measuring station to data base " + e.getMessage());
//
//        }
//    }


//    @Transactional
//    private int[] saveSynMeasuremet(Map<String, SynopticMeasurement> synMstMap,
//                                 Map<MeasuringStation, AirMeasurement> mStMap) throws HibernateException {
//        int[] counters = new int[2];
//        mStMap.entrySet().forEach(mSt -> {
//            AirMeasurement airMeasurement = mSt.getValue();
//            MeasuringStation measuringStation = mSt.getKey();
//            try {
//                airMeasurement.setMeasuringStation(measuringStation);
//                airRepository.save(airMeasurement);
//                LOGGER.info(ANSI_WHITE + "SAVED AIR MEASUREMENT FOR STATION ID -> " + measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
//                counters[0]++;
//                if (synMstMap.keySet().contains(measuringStation.getCity())) {
//                    SynopticMeasurement synopticMeasurement = synMstMap.get(measuringStation.getCity());
//                    synopticMeasurement.setMeasuringStation(measuringStation);
//                    synopticRepository.save(synopticMeasurement);
//                    LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " + measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
//                    counters[1]++;
//                }
//                measuringStationRepository.save(measuringStation);
//            } catch (RuntimeException e) {
//                e.printStackTrace();
//                throw new HibernateException("Can't save stationID: " + measuringStation.getStationId()
//                        + " because of data base error-> " + e.getMessage());
//            }
//        });
//        return counters;
//    }
//
//    private String timeer(Long timeMiliseconds) {
//        DecimalFormat df2 = new DecimalFormat("###.###");
//        return df2.format(timeMiliseconds / 60000.0);
//    }
}
