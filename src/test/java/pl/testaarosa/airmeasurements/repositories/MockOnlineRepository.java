package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;

import java.util.LinkedList;
import java.util.List;

public class MockOnlineRepository {
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();
    private final MockSynopticDtoRepository mockSynopticDtoRepository = new MockSynopticDtoRepository();

    public List<OnlineMeasurementDto> measuringStationOnLineList(){
        List<OnlineMeasurementDto> onlineMeasurementDtoList = new LinkedList<>();
        OnlineMeasurementDto msOnline1 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(2)
                .gegrLatitude(11)
                .gegrLongitude(123)
                .stationName("Poznan-Stoleczna")
                .stationStreet("Stoleczna")
                .stationCity("Poznan")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("voivodeship1")
                .air(mockAirDtoRepository.airMeasurementsDtos().get(2))
                .synoptics(mockSynopticDtoRepository.mockSynopticDtoRepositories().get(2))
                .build();
        OnlineMeasurementDto msOnline2 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(3)
                .gegrLatitude(23)
                .gegrLongitude(221)
                .stationName("Krakow-Starowka")
                .stationStreet("Starowka")
                .stationCity("Krakow")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("voivodeship1")
                .air(mockAirDtoRepository.airMeasurementsDtos().get(3))
                .synoptics(mockSynopticDtoRepository.mockSynopticDtoRepositories().get(3))
                .build();
        OnlineMeasurementDto msOnline3 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(1)
                .gegrLatitude(15)
                .gegrLongitude(15)
                .stationName("Wawrszawa-Centrum")
                .stationStreet("Piekna")
                .stationCity("Warszawa")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("voivodeship1")
                .air(mockAirDtoRepository.airMeasurementsDtos().get(0))
                .synoptics(mockSynopticDtoRepository.mockSynopticDtoRepositories().get(1))
                .build();

        OnlineMeasurementDto msOnline4 = new OnlineMeasurementDto.OnlineMeasurementBuilder()
                .id(1)
                .gegrLatitude(15)
                .gegrLongitude(15)
                .stationName("Wawrszawa-Centrum")
                .stationStreet("Piekna")
                .stationCity("Warszawa")
                .stationDistrict("Dictrict1")
                .stationVoivodeship("voivodeship1")
                .air(mockAirDtoRepository.airMeasurementsDtos().get(0))
                .synoptics(mockSynopticDtoRepository.mockSynopticDtoRepositories().get(1))
                .build();
        onlineMeasurementDtoList.add(msOnline1);
        onlineMeasurementDtoList.add(msOnline2);
        onlineMeasurementDtoList.add(msOnline3);
        onlineMeasurementDtoList.add(msOnline4);
        return onlineMeasurementDtoList;
    }
}
