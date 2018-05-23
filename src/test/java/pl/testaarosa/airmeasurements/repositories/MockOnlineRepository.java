package pl.testaarosa.airmeasurements.repositories;

import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.LevelDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.ArrayList;
import java.util.List;

public class MockOnlineRepository {
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

    public List<MeasuringStationOnLine> resultForOnlineService(){
        List<MeasuringStationOnLine>  measuringStationOnLines = new ArrayList<>();
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
}
