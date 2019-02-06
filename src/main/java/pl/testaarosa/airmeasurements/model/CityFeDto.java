package pl.testaarosa.airmeasurements.model;

import java.util.List;

public class CityFeDto {

    private String name;
    private List<AirMeasurementFeDto> airMeasurementFeDtoList;
    private SynopticMeasurementFeDto synopticMeasurementFeDto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AirMeasurementFeDto> getAirMeasurementFeDtoList() {
        return airMeasurementFeDtoList;
    }

    public void setAirMeasurementFeDtoList(List<AirMeasurementFeDto> airMeasurementFeDtoList) {
        this.airMeasurementFeDtoList = airMeasurementFeDtoList;
    }

    public SynopticMeasurementFeDto getSynopticMeasurementFeDto() {
        return synopticMeasurementFeDto;
    }

    public void setSynopticMeasurementFeDto(SynopticMeasurementFeDto synopticMeasurementFeDto) {
        this.synopticMeasurementFeDto = synopticMeasurementFeDto;
    }
}
