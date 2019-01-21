package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;

import java.util.LinkedList;
import java.util.List;

public class MockOnlineMeasurementRepository {
    private final MockAirMeasurementRepository mockAirDtoRepository = new MockAirMeasurementRepository();
    private final MockSynopticMeasurementRepository mockSynopticDtoRepository = new MockSynopticMeasurementRepository();

    public List<OnlineMeasurementDto> measuringStationOnLineList(){
        List<OnlineMeasurementDto> onlineMeasurementDtoList = new LinkedList<>();
        OnlineMeasurementDto msOnline1 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(1)
                .gegrLatitude(15)
                .gegrLongitude(15)
                .stationName("Wawrszawa-Centrum")
                .stationStreet("Piekna")
                .stationCity("Warszawa")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("mazowieckie")
                .airMs(mockAirDtoRepository.airMeasurements1().get(0))
                .synopticMs(mockSynopticDtoRepository.synopticMeasurementsOrderColdest().get(0))
                .build();
        OnlineMeasurementDto msOnline2 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(1)
                .gegrLatitude(15)
                .gegrLongitude(15)
                .stationName("Wawrszawa-Centrum")
                .stationStreet("Piekna")
                .stationCity("Warszawa")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("mazowieckie")
                .airMs(mockAirDtoRepository.airMeasurements1().get(0))
                .synopticMs(mockSynopticDtoRepository.synopticMeasurementsOrderColdest().get(0))
                .build();
        OnlineMeasurementDto msOnline3 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(2)
                .gegrLatitude(11)
                .gegrLongitude(123)
                .stationName("Poznan-Stoleczna")
                .stationStreet("Stoleczna")
                .stationCity("Poznan")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("wielkopolskie")
                .airMs(mockAirDtoRepository.airMeasurements2().get(2))
                .synopticMs(mockSynopticDtoRepository.synopticMeasurementsOrderHottest().get(2))
                .build();
        OnlineMeasurementDto msOnline4 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(3)
                .gegrLatitude(23)
                .gegrLongitude(221)
                .stationName("Krakow-Starowka")
                .stationStreet("Starowka")
                .stationCity("Krakow")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("malopolskie")
                .airMs(mockAirDtoRepository.airMeasurements2().get(2))
                .synopticMs(mockSynopticDtoRepository.synopticMeasurementsOrderHottest().get(3))
                .build();

        onlineMeasurementDtoList.add(msOnline1);
        onlineMeasurementDtoList.add(msOnline2);
        onlineMeasurementDtoList.add(msOnline3);
        onlineMeasurementDtoList.add(msOnline4);
        return onlineMeasurementDtoList;
    }
}
