package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.measurementsdto.MeasuringStationDto;

import java.util.ArrayList;
import java.util.List;

public class MockMeasuringStationDtoRepository {

    private final MockCityDtoRepository mockCityDtoRepository = new MockCityDtoRepository();

    public List<MeasuringStationDto> measuringStationDtoList(){
        List<MeasuringStationDto> measuringStationDtoList = new ArrayList<>();
        MeasuringStationDto measuringStationDto1 = new MeasuringStationDto();
        measuringStationDto1.setId(1);
        measuringStationDto1.setStationName("Wawrszawa-Centrum");
        measuringStationDto1.setGegrLat("15");
        measuringStationDto1.setGegrLon("15");
        measuringStationDto1.setAddressStreet("Piekna");
        measuringStationDto1.setCityDto(mockCityDtoRepository.cityDtos().get(0));

        MeasuringStationDto measuringStationDto2 = new MeasuringStationDto();
        measuringStationDto2.setId(1);
        measuringStationDto2.setStationName("Wawrszawa-Centrum");
        measuringStationDto2.setGegrLat("15");
        measuringStationDto2.setGegrLon("15");
        measuringStationDto2.setAddressStreet("Piekna");
        measuringStationDto2.setCityDto(mockCityDtoRepository.cityDtos().get(0));

        measuringStationDtoList.add(measuringStationDto1);
        measuringStationDtoList.add(measuringStationDto2);

        return measuringStationDtoList;
    }
}
