package pl.testaarosa.airmeasurements.domain;

import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurements;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "MEASUREMENT_STATION")
public class MeasuringStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measuringStation")
    private List<AirMeasurements> airMeasurementsList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "measuringStation")
    private List<SynopticMeasurements> synopticMeasurements = new ArrayList<>();

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

    public List<AirMeasurements> getAirMeasurementsList() {
        return airMeasurementsList;
    }

    public void setAirMeasurementsList(List<AirMeasurements> airMeasurementsList) {
        this.airMeasurementsList = airMeasurementsList;
    }

    public List<SynopticMeasurements> getSynopticMeasurements() {
        return synopticMeasurements;
    }

    public void setSynopticMeasurements(List<SynopticMeasurements> synopticMeasurements) {
        this.synopticMeasurements = synopticMeasurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasuringStation)) return false;
        MeasuringStation that = (MeasuringStation) o;
        return getStationId() == that.getStationId() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getStationName(), that.getStationName()) &&
                Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLongitude(), that.getLongitude()) &&
                Objects.equals(getStreet(), that.getStreet()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getStationDetails(), that.getStationDetails()) &&
                Objects.equals(getAirMeasurementsList(), that.getAirMeasurementsList()) &&
                Objects.equals(getSynopticMeasurements(), that.getSynopticMeasurements());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getStationId(), getStationName(), getLatitude(), getLongitude(), getStreet(), getCity(), getStationDetails(), getAirMeasurementsList(), getSynopticMeasurements());
    }

    @Override
    public String toString() {
        return "MeasuringStation id: " + id + ", foreign id: " + stationId + ", station name: " + stationName + "\n" + ", latitude=: " + latitude + ", longitude: " + longitude + ", street: " + street + ", city: "
                + city + "\n" + stationDetails +  "\n" + airMeasurementsList + "\n" + synopticMeasurements;
    }
}

