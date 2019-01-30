package pl.testaarosa.airmeasurements.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CITY_NAME")
    private String cityName;
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<AirMeasurement> airMeasurementList = new ArrayList<>();
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<SynopticMeasurement> synopticMeasurementList = new ArrayList<>();

    public City() {
    }

    public City(Long id, String cityName, List<AirMeasurement> airMeasurementList, List<SynopticMeasurement> synopticMeasurementList) {
        this.id = id;
        this.cityName = cityName;
        this.airMeasurementList = airMeasurementList;
        this.synopticMeasurementList = synopticMeasurementList;
    }

    public Long getId() {
        return id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<AirMeasurement> getAirMeasurementList() {
        return airMeasurementList;
    }

    public void setAirMeasurementList(List<AirMeasurement> airMeasurementList) {
        this.airMeasurementList = airMeasurementList;
    }

    public List<SynopticMeasurement> getSynopticMeasurementList() {
        return synopticMeasurementList;
    }

    public void setSynopticMeasurementList(List<SynopticMeasurement> synopticMeasurementList) {
        this.synopticMeasurementList = synopticMeasurementList;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id) &&
                Objects.equals(cityName, city.cityName) &&
                Objects.equals(airMeasurementList, city.airMeasurementList) &&
                Objects.equals(synopticMeasurementList, city.synopticMeasurementList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cityName, airMeasurementList, synopticMeasurementList);
    }
}
