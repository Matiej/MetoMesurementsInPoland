package pl.testaarosa.airmeasurements.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class SynopticMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FOREIGN_ID")
    private int foreignId;
    private String city;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime saveDate;
    private double temperature;
    @Column(name = "WIND_SPEED")
    private double windSpeed;
    @Column(name = "AIR_HUMIDITY")
    private double airHumidity;
    private double pressure;
    private String measurementDate;
    private String measurementHour;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STATION_ID")
    private MeasuringStation measuringStation;

    public SynopticMeasurement() {
    }

    private SynopticMeasurement(SynopticMeasurementsBuilder builder) {
        this.foreignId = builder.foreignId;
        this.city = builder.city;
        this.saveDate = builder.saveDate;
        this.temperature = builder.temperature;
        this.windSpeed = builder.windSpeed;
        this.airHumidity = builder.airHumidity;
        this.pressure = builder.pressure;
        this.measuringStation = builder.measuringStation;
        this.measurementDate = builder.measurementDate;
        this.measurementHour = builder.measurementHour;
    }

    @JsonIgnore
    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
    }

    @JsonIgnore
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

    public String getMeasurementDate() {
        return measurementDate;
    }

    public String getMeasurementHour() {
        return measurementHour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SynopticMeasurement that = (SynopticMeasurement) o;
        return foreignId == that.foreignId &&
                Double.compare(that.temperature, temperature) == 0 &&
                Double.compare(that.windSpeed, windSpeed) == 0 &&
                Double.compare(that.airHumidity, airHumidity) == 0 &&
                Double.compare(that.pressure, pressure) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(city, that.city) &&
                Objects.equals(saveDate, that.saveDate) &&
                Objects.equals(measurementDate, that.measurementDate) &&
                Objects.equals(measurementHour, that.measurementHour) &&
                Objects.equals(measuringStation, that.measuringStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignId, city, saveDate, temperature, windSpeed, airHumidity, pressure, measurementDate, measurementHour, measuringStation);
    }

    @Override
    public String toString() {
        return "SynopticMeasurement id: " + id + ", foreign id=" + foreignId + ", save date: " + saveDate + "\n" + ", temperature: " + temperature + ", windSpeed: " + windSpeed + "\n" + ", airHumidity: " + airHumidity + ", pressure: " + pressure
                + "\n measurement date; " + measurementDate + "\n hour: " + measurementHour + "\n" + "_____________________________" + "\n";
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
        private String measurementDate;
        private String measurementHour;
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

        public SynopticMeasurementsBuilder measurementDate(String measurementDate) {
            this.measurementDate = measurementDate;
            return this;
        }

        public SynopticMeasurementsBuilder measurementHour(String measurementHour) {
            this.measurementHour = measurementHour;
            return this;
        }

        public SynopticMeasurement build() {
            return new SynopticMeasurement(this);
        }
    }
}

