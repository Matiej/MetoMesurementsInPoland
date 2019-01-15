package pl.testaarosa.airmeasurements.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "MEASURING_ST")
public class MeasuringStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FOREIGN_ID")
    private int stationId;
    @Column(name = "STATION_NAME")
    private String stationName;
    private String latitude;
    private String longitude;
    private String street;
    private String city;
    @OneToOne(cascade = CascadeType.ALL)
    private MeasuringStationDetails stationDetails;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measuringStation")
    private List<AirMeasurement> airMeasurementList = new ArrayList<>();
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measuringStation")
    private List<SynopticMeasurement> synopticMeasurements = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = Optional.ofNullable(street).orElse("no data");
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public MeasuringStationDetails getStationDetails() {
        return stationDetails;
    }

    public void setStationDetails(MeasuringStationDetails stationDetails) {
        this.stationDetails = stationDetails;
    }

    public List<AirMeasurement> getAirMeasurementList() {
        return airMeasurementList;
    }

    public void setAirMeasurementList(List<AirMeasurement> airMeasurementList) {
        this.airMeasurementList = airMeasurementList;
    }

    public List<SynopticMeasurement> getSynopticMeasurements() {
        return synopticMeasurements;
    }

    public void setSynopticMeasurements(List<SynopticMeasurement> synopticMeasurements) {
        this.synopticMeasurements = synopticMeasurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasuringStation that = (MeasuringStation) o;
        return stationId == that.stationId &&
                Objects.equals(id, that.id) &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude) &&
                Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(stationDetails, that.stationDetails) &&
                Objects.equals(airMeasurementList, that.airMeasurementList) &&
                Objects.equals(synopticMeasurements, that.synopticMeasurements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stationId, stationName, latitude, longitude, street, city, stationDetails, airMeasurementList, synopticMeasurements);
    }

    @Override
    public String toString() {
        return "MeasuringStation id: " + id + ", foreign id: " + stationId + ", station name: " + stationName + "\n" + ", latitude=: " + latitude + ", longitude: " + longitude + ", street: " + street + ", city: "
                + city + "\n" + stationDetails +  "\n" + airMeasurementList + "\n" + synopticMeasurements;
    }
}

