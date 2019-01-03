package pl.testaarosa.airmeasurements.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
//@Scope(scopeName = "prototype")
public class MeasuringOnlineServicesImpl implements MeasuringOnlineServices {

    @Autowired
    private final MeasurementStationProcessor msProc;

    @Autowired
    public MeasuringOnlineServicesImpl(MeasurementStationProcessor msProc) {
        this.msProc = msProc;
    }

    @Override
    public List<MeasuringStationOnLine> getAllMeasuringStations() throws RestClientException, NoSuchElementException {
        try {
            List<MeasuringStationOnLine> measuringStationOnLines = msProc.fillMeasuringStationListStructure();
            if (measuringStationOnLines.isEmpty()) {
                throw new NoSuchElementException("Can't find any online measuring stations");
            }
            return measuringStationOnLines;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException("External REST API server error! Can't get online measurements for all stations.-> " + e.getMessage());
        }
    }

    @Override
    public List<MeasuringStationOnLine> getGivenCityMeasuringStationsWithSynopticData(String stationCity)
            throws RestClientException, IllegalArgumentException, NoSuchElementException {
        List<MeasuringStationOnLine> measuringStationOnLineList = new ArrayList<>();
        if (stationCity.isEmpty()) {
            throw new IllegalArgumentException("City name can't be empty!");
        } else {
            try {
                measuringStationOnLineList = msProc.fillMeasuringStationListStructure()
                        .stream()
                        .parallel()
                        .filter(c -> c.getStationCity().toLowerCase().contains(stationCity.toLowerCase()))
                        .collect(Collectors.toList());
                if (measuringStationOnLineList.isEmpty()) {
                    throw new NoSuchElementException("Cant't find any stations for city: " + stationCity);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                throw new RestClientException(e.getMessage());
            }
        }
        return measuringStationOnLineList;
    }

    @Override
    public MeasuringStationOnLine getHottestOnlineStation() throws RestClientException, NoSuchElementException {
        try {
            MeasuringStationOnLine measuringStationOnLine = msProc.fillMeasuringStationListStructure()
                    .stream()
                    .parallel()
                    .filter(f -> f.getSynoptics().getTemperature() < 9999)
                    .max(Comparator.comparing(t -> t.getSynoptics().getTemperature()))
                    .orElse(null);
            if (!Optional.ofNullable(measuringStationOnLine).isPresent()) {
                throw new NoSuchElementException("Can't find hottest measurement online");
            }
            return measuringStationOnLine;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException(e.getMessage());
        }
    }

    @Override
    public MeasuringStationOnLine getColdestOnlineStation() throws RestClientException, NoSuchElementException {
        try {
            MeasuringStationOnLine measuringStationOnLine = msProc.fillMeasuringStationListStructure()
                    .stream()
                    .parallel()
                    .filter(f -> f.getSynoptics().getTemperature() < 9999)
                    .min(Comparator.comparing(t -> t.getSynoptics().getTemperature()))
                    .orElse(null);
            if (!Optional.ofNullable(measuringStationOnLine).isPresent()) {
                throw new NoSuchElementException("Can't find coldest measurement online");
            }
            return measuringStationOnLine;
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new RestClientException("External REST API server error! Can't get coldest measurement online for station "
                    + " -> " + e.getMessage());
        }
    }
}
