package pl.testaarosa.airmeasurements.services;

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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//TODO refaktor konkret!.
@Service
public class AddMeasurementsServiceImpl implements AddMeasurementsService {

    private final ApiSupplierRetriever apiSupplierRetriever;
    private final MeasuringStationRepository measuringStationRepository;
    private final AirMeasurementMapper airMapper;
    private final SynopticMeasurementMapper synopticMapper;
    private final SynopticMeasurementRepository synopticRepository;
    private final AirMeasurementRepository airRepository;
    private final MeasuringOnlineServices measuringOnlineServices;

    @Autowired
    public AddMeasurementsServiceImpl(ApiSupplierRetriever apiSupplierRetriever, MeasuringStationRepository measuringStationRepository, AirMeasurementMapper airMapper, SynopticMeasurementMapper synopticMapper, SynopticMeasurementRepository synopticRepository, AirMeasurementRepository airRepository, MeasuringOnlineServices measuringOnlineServices) {
        this.apiSupplierRetriever = apiSupplierRetriever;
        this.measuringStationRepository = measuringStationRepository;
        this.airMapper = airMapper;
        this.synopticMapper = synopticMapper;
        this.synopticRepository = synopticRepository;
        this.airRepository = airRepository;
        this.measuringOnlineServices = measuringOnlineServices;
    }
//TODO ale masakra metoda-> i to ja sam pisalem. ??
    @Transactional
    @Override
    public String addMeasurements(int statioId) throws ExecutionException, InterruptedException {
        int id = 0;
        long startTime1 = System.currentTimeMillis();
        for (MeasuringStationDto measuringStationDto : apiSupplierRetriever.measuringStationApiProcessor().get()) {
            if (measuringStationDto.getId() == statioId) {
                id = measuringStationDto.getId();
            }
        }

        if (id > 0) {
            AirMeasurementsDto airDto = apiSupplierRetriever.airMeasurementsProcessor().get().get(statioId);
            measuringOnlineServices.addAllStations();
            SynopticMeasurementDto synoptic = new SynopticMeasurementDto();
            MeasuringStationDto msDto = new MeasuringStationDto();

            for (MeasuringStationDto measuringStationDto : apiSupplierRetriever.measuringStationApiProcessor().get()) {
                if (measuringStationDto.getId() == statioId) {
                    msDto = measuringStationDto;
                    synoptic = Optional.ofNullable(apiSupplierRetriever.synopticMeasurementProcessor().get()
                            .get(msDto.getCityDto().getCityName()))
                            .orElse(emptyObj());
                }
            }

            MeasuringStation measuringStation = measuringStationRepository.findByStationId(statioId);

            AirMeasurements airMeasurements = airMapper.mapToAirMeasurements(airDto);
            airRepository.save(airMeasurements);
            measuringStation.getAirMeasurementsList().add(airMeasurements);

            SynopticMeasurements synopticMeasurements = synopticMapper.maptToSynopticMeasurement(synoptic);
            synopticRepository.save(synopticMeasurements);
            measuringStation.getSynopticMeasurements().add(synopticMeasurements);
            airMeasurements.setMeasuringStation(measuringStation);
            synopticMeasurements.setMeasuringStation(measuringStation);
            measuringStationRepository.save(measuringStation);
            long endTime1 = System.currentTimeMillis();
            DecimalFormat df2 = new DecimalFormat("###.###");
            double execution = (endTime1 - startTime1) / 60000.0;
            return "Measurement execution time: " + df2.format(execution) + " minutes, saved as below: \n" + measuringStation + "\n" + "######################################################################################### \n" + synopticMeasurements + "\n" + "######################################################################################### \n" + airMeasurements;
        }
        CompletableFuture.allOf().join();
        return " No data for measuring station Id: " + statioId;
    }
//TODO zrobic normallnie tj zwroci obiekt nie stringi-> fuknyjnie i jakoś rodzielić to na kilka metod. Moze AIRService, StationSevice i SynopticService
    @Transactional
    @Override
    public String addMeasurementsAllStations() throws ExecutionException, InterruptedException {
        measuringOnlineServices.addAllStations();
        List<MeasuringStationDto> mSDtoList = apiSupplierRetriever.measuringStationApiProcessor().get();
        long startTime1 = System.currentTimeMillis();
        for (MeasuringStationDto msDto : mSDtoList) {
            int id = msDto.getId();
            AirMeasurementsDto airMeasurementsDto = apiSupplierRetriever.airMeasurementsProcessor().get().get(id);
            String cityName = Optional.ofNullable(msDto.getCityDto().getCityName()).orElse("no data");
            SynopticMeasurementDto synoptic = Optional.ofNullable(apiSupplierRetriever.synopticMeasurementProcessor().get()
                    .get(cityName))
                    .orElse(emptyObj());
            MeasuringStation measuringStation = measuringStationRepository.findByStationId(id);

            AirMeasurements airMeasurements = airMapper.mapToAirMeasurements(airMeasurementsDto);
            airRepository.save(airMeasurements);
            measuringStation.getAirMeasurementsList().add(airMeasurements);

            SynopticMeasurements synopticMeasurements = synopticMapper.maptToSynopticMeasurement(synoptic);
            synopticRepository.save(synopticMeasurements);
            measuringStation.getSynopticMeasurements().add(synopticMeasurements);

            airMeasurements.setMeasuringStation(measuringStation);
            synopticMeasurements.setMeasuringStation(measuringStation);
            measuringStationRepository.save(measuringStation);
        }
        CompletableFuture.allOf().join();
        long endTime1 = System.currentTimeMillis();
        DecimalFormat df2 = new DecimalFormat("###.###");
        double execution = (endTime1 - startTime1) / 60000.0;
        return "Measured time of downloading data: " + df2.format(execution) + " minutes\n" + mSDtoList.size() + " measurements added to data base correct";
    }

    private SynopticMeasurementDto emptyObj() {
        return new SynopticMeasurementDto(9999, "->>no data available<<-", 9999.0, 9999.0, 9999.0, 9999.0);
    }
}
