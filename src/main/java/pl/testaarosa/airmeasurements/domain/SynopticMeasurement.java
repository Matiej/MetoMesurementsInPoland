package pl.testaarosa.airmeasurements.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class SynopticMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FOREIGN_ID")
    private int foreignId;
    private String cityName;
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
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "synopticMeasurements", targetEntity = MeasuringStation.class)
    private List<MeasuringStation> measuringStation = new ArrayList<>();
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "CITY_ID")
    private City city;

    public SynopticMeasurement() {
    }

    private SynopticMeasurement(SynopticMeasurementsBuilder builder) {
        this.foreignId = builder.foreignId;
        this.cityName = builder.cityName;
        this.saveDate = builder.saveDate;
        this.temperature = builder.temperature;
        this.windSpeed = builder.windSpeed;
        this.airHumidity = builder.airHumidity;
        this.pressure = builder.pressure;
        this.measuringStation = new ArrayList<>(builder.measuringStation);
        this.measurementDate = builder.measurementDate;
        this.measurementHour = builder.measurementHour;
        this.city = builder.city;
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

    public String getCityName() {
        return cityName;
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

    public List<MeasuringStation> getMeasuringStation() {
        return measuringStation;
    }

    @JsonIgnore
    public void setMeasuringStation(List<MeasuringStation> measuringStation) {
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

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
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
                Objects.equals(cityName, that.cityName) &&
                Objects.equals(saveDate, that.saveDate) &&
                Objects.equals(measurementDate, that.measurementDate) &&
                Objects.equals(measurementHour, that.measurementHour) &&
                Objects.equals(measuringStation, that.measuringStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foreignId, cityName, saveDate, temperature, windSpeed, airHumidity, pressure, measurementDate, measurementHour, measuringStation);
    }

    @Override
    public String toString() {
        return "SynopticMeasurement id: " + id + ", foreign id=" + foreignId + ", cityName " + cityName + ", save date: " + saveDate + "\n" + ", temperature: " + temperature + ", windSpeed: " + windSpeed + "\n" + ", airHumidity: " + airHumidity + ", pressure: " + pressure
                + "\n measurement date; " + measurementDate + "\n hour: " + measurementHour + "\n" + "_____________________________" + "\n";
    }

    public static class SynopticMeasurementsBuilder {
        private Long id;
        private int foreignId;
        private LocalDateTime saveDate;
        private String cityName;
        private double temperature;
        private double windSpeed;
        private double airHumidity;
        private double pressure;
        private String measurementDate;
        private String measurementHour;
        private List<MeasuringStation> measuringStation = new ArrayList<>();
        private City city;

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

        public SynopticMeasurementsBuilder city(String cityName) {
            this.cityName = cityName;
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

        public SynopticMeasurementsBuilder measuringStation(List<MeasuringStation> measuringStation) {
            this.measuringStation.addAll(measuringStation);
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

        public SynopticMeasurementsBuilder city(City city) {
            this.city = city;
            return this;
        }

        public SynopticMeasurement build() {
            return new SynopticMeasurement(this);
        }
    }
}

