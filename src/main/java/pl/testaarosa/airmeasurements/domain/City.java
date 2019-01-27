package pl.testaarosa.airmeasurements.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CITY_NAME")
    private String cityName;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "AIRMST_CITY",
            joinColumns = {@JoinColumn(name = "CITY_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AIRMST_ID", referencedColumnName = "ID")})
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
}
