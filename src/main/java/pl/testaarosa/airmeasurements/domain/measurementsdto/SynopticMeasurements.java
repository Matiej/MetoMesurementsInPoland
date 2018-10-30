package pl.testaarosa.airmeasurements.domain.measurementsdto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class SynopticMeasurements {
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
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATION_ID")
    private MeasuringStation measuringStation;

    public SynopticMeasurements() {
    }

    private SynopticMeasurements(SynopticMeasurementsBuilder builder) {
        this.foreignId = builder.foreignId;
        this.city = builder.city;
        this.saveDate = builder.saveDate;
        this.temperature = builder.temperature;
        this.windSpeed = builder.windSpeed;
        this.airHumidity = builder.airHumidity;
        this.pressure = builder.pressure;
        this.measuringStation = builder.measuringStation;
    }

    public Long getId() {
        return id;
    }

    public int getForeignId() {
        return foreignId;
    }

    public String getCity() {
        return city;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getAirHumidity() {
        return airHumidity;
    }

    public double getPressure() {
        return pressure;
    }

    public MeasuringStation getMeasuringStation() {
        return measuringStation;
    }

    @JsonIgnore
    public void setMeasuringStation(MeasuringStation measuringStation) {
        this.measuringStation = measuringStation;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynopticMeasurements)) return false;
        SynopticMeasurements that = (SynopticMeasurements) o;
        return getForeignId() == that.getForeignId() &&
                Double.compare(that.getTemperature(), getTemperature()) == 0 &&
                Double.compare(that.getWindSpeed(), getWindSpeed()) == 0 &&
                Double.compare(that.getAirHumidity(), getAirHumidity()) == 0 &&
                Double.compare(that.getPressure(), getPressure()) == 0 &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getSaveDate(), that.getSaveDate()) &&
                Objects.equals(getMeasuringStation(), that.getMeasuringStation());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getForeignId(), getCity(), getSaveDate(), getTemperature(), getWindSpeed(), getAirHumidity(), getPressure(), getMeasuringStation());
    }

    @Override
    public String toString() {
        return "SynopticMeasurement id: " + id + ", foreign id=" + foreignId + ", save date: " + saveDate + "\n" + ", temperature: " + temperature + ", windSpeed: " + windSpeed + "\n" + ", airHumidity: " + airHumidity + ", pressure: " + pressure + "\n" + "_____________________________" + "\n";
    }

    public static class SynopticMeasurementsBuilder {
        private Long id;
        private int foreignId;
        private LocalDateTime saveDate;
        private String city;
        private double temperature;
        private double windSpeed;
        private double airHumidity;
        private double pressure;
        private MeasuringStation measuringStation;

        public SynopticMeasurementsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public SynopticMeasurementsBuilder foreignId(int foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public SynopticMeasurementsBuilder saveDate(LocalDateTime saveDate) {
            this.saveDate = saveDate;
            return this;
        }

        public SynopticMeasurementsBuilder city(String city) {
            this.city = city;
            return this;
        }

        public SynopticMeasurementsBuilder temperature(double temperature) {
            this.temperature = temperature;
            return this;
        }

        public SynopticMeasurementsBuilder windSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
            return this;
        }

        public SynopticMeasurementsBuilder airHumidity(double airHumidity) {
            this.airHumidity = airHumidity;
            return this;
        }

        public SynopticMeasurementsBuilder pressure(double pressure) {
            this.pressure = pressure;
            return this;
        }

        public SynopticMeasurementsBuilder measuringStation(MeasuringStation measuringStation) {
            this.measuringStation = measuringStation;
            return this;
        }

        public SynopticMeasurements build() {
            return new SynopticMeasurements(this);
        }
    }
}

