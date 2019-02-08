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
import pl.testaarosa.airmeasurements.services.emailService.EmailNotifierService;
import pl.testaarosa.airmeasurements.services.reportService.WorkBookReportGenerator;

import javax.transaction.Transactional;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
public class AddMeasurementsServiceImpl implements AddMeasurementsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AddMeasurementsServiceImpl.class);
    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationRepository measuringStationRepository;
    private final SynopticMeasurementRepository synopticRepository;
    private final AirMeasurementRepository airRepository;
    private final EmailNotifierService emailNotifierService;
    private final CityRepository cityRepository;
    private final WorkBookReportGenerator workBookReportGenerator;

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository,
                                      SynopticMeasurementRepository synopticRepository, AirMeasurementRepository airRepository,
                                      EmailNotifierService emailNotifierService,
                                      CityRepository cityRepository, WorkBookReportGenerator workBookReportGenerator) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.emailNotifierService = emailNotifierService;
        this.cityRepository = cityRepository;
        this.workBookReportGenerator = workBookReportGenerator;
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
            throw new RestClientException("Can't find any synoptic measurement because of REST API error-> " + e.getMessage());
        }
        int counter = saveSynMeasurement(synopticMeasurementMap, mStResponseMap, false);

        String timeer = timeer(System.currentTimeMillis() - startTime1);
        LOGGER.info("SAVED TOTAL MEASUREMENTS FOR STATION ID: " + stationId + "\nAIR MEASUREMENTS ->" + mStResponseMap.size() +
                " \nSYNOPTIC MEASUREMENTS-> " + counter + " \n TOTAL TIME: " + timeer);
        return new ArrayList<>(mStResponseMap.keySet()).get(0);
    }

    @Transactional
    @Override
    public List<MeasuringStation> addMeasurementsAllStations() throws RestClientException, HibernateException {
        long startTime1 = System.currentTimeMillis();
        List<MeasuringStation> mSList = new ArrayList<>();
        LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap = new LinkedHashMap<>();
        LinkedHashMap<MeasuringStation, AirMeasurement> mStResponseMap = new LinkedHashMap<>();
        mStResponseMap.putAll(saveAirMeasurementSt(apiSupplierRetriever.airMeasurementsAndStProcessor()));
        synopticMeasurementMap.putAll(apiSupplierRetriever.synopticMeasurementProcessor());
        saveSynMeasurement(synopticMeasurementMap, mStResponseMap, true);
        mSList.addAll(mStResponseMap.keySet());
        String timeer = timeer(System.currentTimeMillis() - startTime1);
        String[] shortMess = {String.valueOf(synopticMeasurementMap.size()), String.valueOf(mSList.size()), timeer};
        auxAddService(shortMess, mSList, synopticMeasurementMap, mStResponseMap);
        LOGGER.info("SAVED AIR MEASUREMENTS ->" + mSList.size() +
                " \nSYNOPTIC MEASUREMENTS-> " + synopticMeasurementMap.size() + " \n TOTAL TIME: " + timeer(System.currentTimeMillis()-startTime1));
        return mSList;
    }

    @Transactional
    private Map<MeasuringStation, AirMeasurement> saveAirMeasurementSt(Map<MeasuringStation, AirMeasurement> mStMap) throws HibernateException {
        Map<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
        mStMap.entrySet()
                .forEach(st -> {
                    AirMeasurement airMeasurement = st.getValue();
                    MeasuringStation measuringStation = st.getKey();
                    City city = new City();
                    try {
                        if (!measuringStationRepository.existsAllByStationId(measuringStation.getStationId())) {
                            airMeasurement.setMeasuringStation(measuringStation);
                            measuringStation.getAirMeasurementList().add(airMeasurement);
                            airRepository.save(airMeasurement);
                            LOGGER.info(ANSI_WHITE + "SAVED AIR MEASUREMENT FOR STATION ID -> " +
                                    measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                            measurementMap.put(measuringStationRepository.save(measuringStation), airMeasurement);
                                 LOGGER.info(ANSI_WHITE + "SAVED NEW MEASURING STATION ID -> " +
                                    measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                        } else {
                            MeasuringStation stationFromDb = measuringStationRepository.findByStationId(measuringStation.getStationId());
                            airMeasurement.setMeasuringStation(stationFromDb);
                            stationFromDb.getAirMeasurementList().add(airMeasurement);
                            airRepository.save(airMeasurement);
                            LOGGER.info(ANSI_GREEN + "SAVED AIR MEASUREMENT FOR STATION ID -> " +
                                    measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                            measurementMap.put(stationFromDb, airMeasurement);
                        }
                        if (!cityRepository.existsAllByCityName(measuringStation.getCity())) {
                            city.setCityName(measuringStation.getCity());
                            city.getAirMeasurementList().add(airMeasurement);
                            airMeasurement.setCity(city);
//                            airRepository.save(airMeasurement);
                            cityRepository.save(city);
                            LOGGER.info("SAVED NEW CITY: " + city.getCityName());
                        } else {
                            City oneByCityName = cityRepository.findOneByCityName(measuringStation.getCity());
                            oneByCityName.getAirMeasurementList().add(airMeasurement);
                            airMeasurement.setCity(oneByCityName);
                            airRepository.save(airMeasurement);
//                            cityRepository.save(oneByCityName);
                        }
                    } catch (HibernateException e) {
                        throw new RuntimeException("Can't save station because of data base error");
                    }
                });
        return measurementMap;
    }


    @Transactional
    private int saveSynMeasurement(Map<String, SynopticMeasurement> synMstMap,
                                   Map<MeasuringStation, AirMeasurement> mStMap, boolean forAllMeasurements) throws HibernateException {
        AtomicInteger counters = new AtomicInteger();
        List<SynopticMeasurement> tmpSynList = new ArrayList<>();
        try {
            mStMap.forEach((measuringStation, value) -> {
                if (synMstMap.keySet().contains(measuringStation.getCity())) {
                    SynopticMeasurement synopticMeasurement = synMstMap.get(measuringStation.getCity());
                    City oneByCityName = cityRepository.findOneByCityName(measuringStation.getCity());
                    tmpSynList.add(synopticMeasurement);

                    synopticMeasurement.getMeasuringStation().add(measuringStation);
                    measuringStation.getSynopticMeasurements().add(synopticMeasurement);
//                    measuringStationRepository.save(measuringStation);
                    synopticRepository.save(synopticMeasurement);

                    synopticMeasurement.setCity(oneByCityName);
                    oneByCityName.getSynopticMeasurementList().add(synopticMeasurement);
                    cityRepository.save(oneByCityName);
                    LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT FOR STATION ID -> " +
                            measuringStation.getStationId() + " IN THE CITY -> " + measuringStation.getCity() + ANSI_RESET);
                    counters.getAndIncrement();
                }
            });
            if (forAllMeasurements) {
                synMstMap.forEach((key, value) -> {
                    if (tmpSynList.stream().noneMatch(value::equals)) {
                        City city = null;
                        if (!cityRepository.existsAllByCityName(value.getCityName())) {
                            city = new City();
                            city.setCityName(value.getCityName());
                            LOGGER.info("SAVED NEW CITY: " + city.getCityName());
                        } else {
                            city = cityRepository.findOneByCityName(value.getCityName());
                        }
                        city.getSynopticMeasurementList().add(value);
                        value.setCity(city);
//                        synopticRepository.save(value);
                        cityRepository.save(city);
                        LOGGER.info(ANSI_PURPLE + "SAVED SYNOPTIC MEASUREMENT THAT DOESN'T HAVE AIR MEASUREMENT STATION. CITY -> "
                                + value.getCityName() + ANSI_RESET);
                        counters.getAndIncrement();
                    }
                });
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't save synoptic measurements because of data base error");
        }
        return counters.get();
    }

    private void auxAddService(String[] shortMsg, List<MeasuringStation> mStList,
                               LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap,
                               LinkedHashMap<MeasuringStation, AirMeasurement> mStResponseMap) {
        //TODO use callable to take back email report
//        Thread mailServiceThread = new Thread(()-> {
//            emailNotifierService.sendEmailAfterDownloadMeasurementsN(mStList, shortMsg);
//            LOGGER.info(ANSI_WHITE+"EMAIL AUXILIARY SERVICE JOB DONE"+ANSI_RESET);
//        });
        Thread reportServiceThread = new Thread(() -> {
            LOGGER.info(ANSI_BOLD+"Preparing email with xml report after successful download all measurement data from external API"+ANSI_RESET);
            File xmlAddAllMeasurementsReport = workBookReportGenerator.createXMLAddAllMeasurementsReport(synopticMeasurementMap, mStResponseMap);
            LOGGER.info(ANSI_BOLD+"XML REPORT AUXILIARY SERVICE JOB DONE"+ANSI_RESET);
            emailNotifierService.sendEmailAfterDownloadMeasurementsWithReport(xmlAddAllMeasurementsReport,shortMsg);
            LOGGER.info(ANSI_WHITE+"EMAIL AUXILIARY SERVICE JOB DONE"+ANSI_RESET);
        });
        reportServiceThread.start();
//        mailServiceThread.start();
    }

    private String timeer(Long timeMiliseconds) {
        DecimalFormat df2 = new DecimalFormat("###.###");
        return df2.format(timeMiliseconds / 60000.0);
    }
}
