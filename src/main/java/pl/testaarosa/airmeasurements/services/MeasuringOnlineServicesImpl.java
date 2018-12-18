package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationDetails;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationDetailsMapper;
import pl.testaarosa.airmeasurements.mapper.MeasuringStationMapper;
import pl.testaarosa.airmeasurements.repositories.MeasuringStationRepository;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
//@Scope(scopeName = "prototype")
public class MeasuringOnlineServicesImpl implements MeasuringOnlineServices {

    private final MeasurementStationProcessor msProc;
    private final MeasuringStationRepository stRepository;
    private final MeasuringStationMapper stMapper;
    private final MeasuringStationDetailsMapper staDetMapper;
    private final ApiSupplierRetriever apiSupplierRetriver;

    @Autowired
    public MeasuringOnlineServicesImpl(MeasurementStationProcessor msProc, MeasuringStationRepository stRepository, MeasuringStationMapper stMapper, MeasuringStationDetailsMapper staDetMapper, ApiSupplierRetriever apiSupplierRetriver) {
        this.msProc = msProc;
        this.stRepository = stRepository;
        this.stMapper = stMapper;
        this.staDetMapper = staDetMapper;
        this.apiSupplierRetriver = apiSupplierRetriver;
    }

    @Transactional
    @Override
    public List<MeasuringStation> addAllStations() throws ExecutionException, InterruptedException {
        List<MeasuringStation> measuringStationList = new LinkedList<>();
        for (MeasuringStationDto measuringStationDto : apiSupplierRetriver.measuringStationApiProcessor().get()) {
            MeasuringStation measuringStation = stMapper.mapToMeasuringSt(measuringStationDto);
            measuringStationList.add(measuringStation);
            int id = measuringStationDto.getId();
            if (!stRepository.existsAllByStationId(id)) {
                MeasuringStationDetails stDetails = staDetMapper.mapToStationDetails(measuringStationDto);
                measuringStation.setStationDetails(stDetails);
                stRepository.save(measuringStation);
            }
        }
        return measuringStationList;
    }

    @Override
    public List<MeasuringStationOnLine> getAllMeasuringStations() throws ExecutionException, InterruptedException {
        return msProc.fillMeasuringStationListStructure();
    }

    @Override
    public List<MeasuringStationOnLine> getGivenCityMeasuringStationsWithSynopticData(String stationCity) throws ExecutionException, InterruptedException {
        return msProc.fillMeasuringStationListStructure()
                .stream()
                .parallel()
                .filter(c -> c.getStationCity().toLowerCase().contains(stationCity.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public MeasuringStationOnLine getHottestOnlineStation() throws ExecutionException, InterruptedException {
        return msProc.fillMeasuringStationListStructure()
                .stream()
                .parallel()
                .filter(f -> f.getSynoptics().getTemperature() < 9999)
                .max(Comparator.comparing(t -> t.getSynoptics().getTemperature()))
                .orElse(null);
    }

    @Override
    public MeasuringStationOnLine getColdestOnlineStation() throws ExecutionException, InterruptedException {
        return msProc.fillMeasuringStationListStructure()
                .stream()
                .parallel()
                .filter(f -> f.getSynoptics().getTemperature() < 9999)
                .min(Comparator.comparing(t -> t.getSynoptics().getTemperature()))
                .orElse(null);
    }
}
