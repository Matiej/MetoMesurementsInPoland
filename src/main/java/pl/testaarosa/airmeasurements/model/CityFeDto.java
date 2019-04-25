package pl.testaarosa.airmeasurements.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.LinkedList;
import java.util.List;

@ApiModel(description = "All data about City. It is model for angular and other front usage")
public class CityFeDto {

    private String name;
    @ApiModelProperty(notes = "All air measurements for this city")
    private List<AirMeasurement> airMeasurementList = new LinkedList<>();
    @ApiModelProperty(notes = "All synoptic measurements for this city")
    private SynopticMeasurement synopticMeasurement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AirMeasurement> getAirMeasurementList() {
        return airMeasurementList;
    }

    public void setAirMeasurementList(List<AirMeasurement> airMeasurementList) {
        this.airMeasurementList = airMeasurementList;
    }

    public SynopticMeasurement getSynopticMeasurement() {
        return synopticMeasurement;
    }

    public void setSynopticMeasurement(SynopticMeasurement synopticMeasurement) {
        this.synopticMeasurement = synopticMeasurement;
    }
}
