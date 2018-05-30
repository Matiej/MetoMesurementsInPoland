package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.LevelDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MockOnlineRepository {
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();
    private final MockSynopticDtoRepository mockSynopticDtoRepository = new MockSynopticDtoRepository();

    public List<MeasuringStationOnLine> measuringStationOnLineList(){
        List<MeasuringStationOnLine> measuringStationOnLineList = new LinkedList<>();
        MeasuringStationOnLine msOnline1 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
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
        MeasuringStationOnLine msOnline2 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
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
        MeasuringStationOnLine msOnline3 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
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

        MeasuringStationOnLine msOnline4 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
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
        measuringStationOnLineList.add(msOnline1);
        measuringStationOnLineList.add(msOnline2);
        measuringStationOnLineList.add(msOnline3);
        measuringStationOnLineList.add(msOnline4);
        return measuringStationOnLineList;
    }
}
