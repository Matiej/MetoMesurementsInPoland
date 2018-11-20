package pl.testaarosa.airmeasurements.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SYNOPTIC_TEMPORARY_MEASUREMENETS")
public class SynopticMeasurementsTmp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FOREIGN_ID")
    private int foreignId;
    private String city;
    private LocalDateTime saveDate;
    private double temperature;
    @Column(name = "WIND_SPEED")
    private double windSpeed;
    @Column(name = "AIR_HUMIDITY")
    private double airHumidity;
    private double pressure;
    private String measurementDate;
    private String measurementHour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getForeignId() {
        return foreignId;
    }

    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public double getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(double airHumidity) {
        this.airHumidity = airHumidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public String getMeasurementHour() {
        return measurementHour;
    }

    public void setMeasurementHour(String measurementHour) {
        this.measurementHour = measurementHour;
    }
}
