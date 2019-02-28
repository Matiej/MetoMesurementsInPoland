package pl.testaarosa.airmeasurements.model;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.LinkedList;
import java.util.List;

public class CityFeDto {

    private String name;
    private List<AirMeasurement> airMeasurementList = new LinkedList<>();
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
