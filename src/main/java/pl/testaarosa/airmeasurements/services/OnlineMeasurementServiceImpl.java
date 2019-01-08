package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RED;
import static pl.testaarosa.airmeasurements.services.ConsolerData.ANSI_RESET;

@Service
//@Scope(scopeName = "prototype")
public class OnlineMeasurementServiceImpl implements OnlineMeasurementService {

    private final OnlineMeasurementProcessor msProc;
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineMeasurementServiceImpl.class);

    @Autowired
    public OnlineMeasurementServiceImpl(OnlineMeasurementProcessor msProc) {
        this.msProc = msProc;
    }

    @Override
    @Cacheable(value = "measuringStCash")
    public List<OnlineMeasurementDto> getAllMeasuringStations() throws RestClientException, NoSuchElementException {
        long start = System.currentTimeMillis();
        try {
            List<OnlineMeasurementDto> onlineMeasurementDtos = msProc.fillMeasuringStationListStructure();
            if (onlineMeasurementDtos.isEmpty()) {
                throw new NoSuchElementException("Can't find any online measuring stations");
            }
            LOGGER.info(ANSI_RED + "Total time-> " + (System.currentTimeMillis()-start) + ANSI_RESET);
            return onlineMeasurementDtos;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException("External REST API server error! Can't get online measurements for all stations.-> " + e.getMessage());
        }
    }

    @Override
    public List<OnlineMeasurementDto> getGivenCityMeasuringStationsWithSynopticData(String stationCity)
            throws RestClientException, IllegalArgumentException, NoSuchElementException {
        long start = System.currentTimeMillis();
        List<OnlineMeasurementDto> onlineMeasurementDtoList;
        if (stationCity.isEmpty()) {
            throw new IllegalArgumentException("City name can't be empty!");
        } else {
            try {
                onlineMeasurementDtoList = msProc.fillMeasuringStationListStructure()
                        .stream()
                        .filter(o -> Optional.ofNullable(o.getStationCity()).isPresent())
                        .filter(c -> c.getStationCity().toLowerCase().contains(stationCity.toLowerCase()))
                        .collect(toList());
                if (onlineMeasurementDtoList.isEmpty()) {
                    throw new NoSuchElementException("Cant't find any stations for city: " + stationCity);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                throw new RestClientException(e.getMessage());
            }
        }
        LOGGER.info(ANSI_RED + "Total time-> " + (System.currentTimeMillis()-start) + ANSI_RESET);
        return onlineMeasurementDtoList;
    }

    @Override
    public OnlineMeasurementDto getHottestOnlineStation() throws RestClientException, NoSuchElementException {
        long start = System.currentTimeMillis();
        try {
            OnlineMeasurementDto onlineMeasurementDto = msProc.fillMeasuringStationListStructure()
                    .stream()
                    .filter(s-> Optional.ofNullable(s.getSynopticMs()).isPresent())
                    .max(Comparator.comparing(t -> t.getSynopticMs().getTemperature()))
                    .orElse(null);
            if (!Optional.ofNullable(onlineMeasurementDto).isPresent()) {
                throw new NoSuchElementException("Can't find hottest measurement online");
            }
            LOGGER.info(ANSI_RED + "Total time-> " + (System.currentTimeMillis()- start) + ANSI_RESET);
            return onlineMeasurementDto;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException(e.getMessage());
        }
    }

    @Override
    public OnlineMeasurementDto getColdestOnlineStation() throws RestClientException, NoSuchElementException {
        long start = System.currentTimeMillis();
        try {
            OnlineMeasurementDto onlineMeasurementDto = msProc.fillMeasuringStationListStructure()
                    .stream()
                    .filter(s-> Optional.ofNullable(s.getSynopticMs()).isPresent())
                    .min(Comparator.comparing(t -> t.getSynopticMs().getTemperature()))
                    .orElse(null);
            if (!Optional.ofNullable(onlineMeasurementDto).isPresent()) {
                throw new NoSuchElementException("Can't find coldest measurement online");
            }
            LOGGER.info(ANSI_RED + "Total time-> " + (System.currentTimeMillis()-start) + ANSI_RESET);
            return onlineMeasurementDto;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException("External REST API server error! Can't get coldest measurement online for station "
                    + " -> " + e.getMessage());
        }
    }
}
