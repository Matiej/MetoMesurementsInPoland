package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.LevelDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.ArrayList;
import java.util.List;

public class MockOnlineRepository {
    private final MockAirDtoRepository mockAirDtoRepository = new MockAirDtoRepository();
    private final MockSynopticDtoRepository mockSynopticDtoRepository = new MockSynopticDtoRepository();

    public List<MeasuringStationOnLine> resultForOnlineController() {
        List<MeasuringStationOnLine> measuringStationOnLines = new ArrayList<>();

        LevelDto lvl = new LevelDto.Builder().id(1).indexLevelName("test name").build();
        AirMeasurementsDto air = new AirMeasurementsDto();
        air.setId(1);
        air.setStCalcDate("2018-04-26 12:06:33");
        air.setStIndexLevel(lvl);
        air.setSo2IndexLevel(lvl);
        air.setSo2SourceDataDate("2018-04-26 12:06:33");
        air.setNo2IndexLevel(lvl);
        air.setCoIndexLevel(lvl);
        air.setCoSourceDataDate("2018-04-26 12:06:33");
        air.setPm10IndexLevel(lvl);
        air.setPm25IndexLevel(lvl);
        air.setO3IndexLevel(lvl);
        air.setC6h6IndexLevel(lvl);

        SynopticMeasurementDto syn = new SynopticMeasurementDto(9999,
                "->>no data available<<-",
                9999,
                9999,
                9999,
                9999);

        MeasuringStationOnLine msOnline = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
                .id(1)
                .gegrLatitude(0.0)
                .gegrLongitude(0.0)
                .stationName("Test station")
                .stationStreet("test")
                .stationCity("te")
                .stationDistrict("di")
                .stationVoivodeship("vo")
                .air(air)
                .synoptics(syn)
                .build();
        measuringStationOnLines.add(msOnline);

        return measuringStationOnLines;
    }

    public List<MeasuringStationOnLine> resultForOnlineService() {
        List<MeasuringStationOnLine> measuringStationOnLines = new ArrayList<>();
        LevelDto lvl = new LevelDto.Builder().id(1).indexLevelName("test name").build();
        AirMeasurementsDto air = new AirMeasurementsDto();
        air.setId(1);
        air.setStCalcDate("2018-04-26 12:06:33");
        air.setStIndexLevel(lvl);
        air.setSo2IndexLevel(lvl);
        air.setSo2SourceDataDate("2018-04-26 12:06:33");
        air.setNo2IndexLevel(lvl);
        air.setCoIndexLevel(lvl);
        air.setCoSourceDataDate("2018-04-26 12:06:33");
        air.setPm10IndexLevel(lvl);
        air.setPm25IndexLevel(lvl);
        air.setO3IndexLevel(lvl);
        air.setC6h6IndexLevel(lvl);

        SynopticMeasurementDto syn = new SynopticMeasurementDto(1, "->>no data available<<-", 25, 9999, 9999, 9999);
        SynopticMeasurementDto synM = new SynopticMeasurementDto(2, "->>no data available<<-", 1, 3, 4, 3);
        SynopticMeasurementDto synL = new SynopticMeasurementDto(3, "->>no data available<<-", 16, 9999, 9999, 9999);

        MeasuringStationOnLine msOnline = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
                .id(1)
                .gegrLatitude(0.0)
                .gegrLongitude(0.0)
                .stationName("Test station")
                .stationStreet("test")
                .stationCity("Moskwa")
                .stationDistrict("di")
                .stationVoivodeship("vo")
                .air(air)
                .synoptics(syn)
                .build();
        MeasuringStationOnLine msOnline1 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
                .id(2)
                .gegrLatitude(0.0)
                .gegrLongitude(0.0)
                .stationName("Test station")
                .stationStreet("test")
                .stationCity("te")
                .stationDistrict("di")
                .stationVoivodeship("vo")
                .air(air)
                .synoptics(synL)
                .build();
        MeasuringStationOnLine msOnline2 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
                .id(3)
                .gegrLatitude(0.0)
                .gegrLongitude(0.0)
                .stationName("Test station")
                .stationStreet("test")
                .stationCity("te")
                .stationDistrict("di")
                .stationVoivodeship("vo")
                .air(air)
                .synoptics(synM)
                .build();
        measuringStationOnLines.add(msOnline);
        measuringStationOnLines.add(msOnline1);
        measuringStationOnLines.add(msOnline2);

        return measuringStationOnLines;
    }

    public List<MeasuringStationOnLine> measuringStationOnLineList(){
        List<MeasuringStationOnLine> measuringStationOnLineList = new ArrayList<>();
        MeasuringStationOnLine msOnline1 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
                .id(11)
                .gegrLatitude(11)
                .gegrLongitude(123)
                .stationName("Poznan-Stoleczna")
                .stationStreet("Stoleczna")
                .stationCity("Poznan")
                .stationDistrict("di")
                .stationVoivodeship("vo")
                .air(mockAirDtoRepository.airMeasurementsDtos().get(1))
                .synoptics(mockSynopticDtoRepository.mockSynopticDtoRepositories().get(2))
                .build();
        MeasuringStationOnLine msOnline2 = new MeasuringStationOnLine.MeasuringStationOnLineBuilder()
                .id(2)
                .gegrLatitude(23)
                .gegrLongitude(221)
                .stationName("Krakow-Starowka")
                .stationStreet("Starowka")
                .stationCity("Krakow")
                .stationDistrict("di")
                .stationVoivodeship("vo")
                .air(mockAirDtoRepository.airMeasurementsDtos().get(0))
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
