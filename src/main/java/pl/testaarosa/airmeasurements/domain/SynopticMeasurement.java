package pl.testaarosa.airmeasurements.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;

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
    private LocalDateTime measurementDate;
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

    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }


    @Override
    public String toString() {
        return "SynopticMeasurement id: " + id + ", foreign id=" + foreignId + ", cityName " + cityName + ", save date: " + saveDate + "\n" + ", temperature: " + temperature + ", windSpeed: " + windSpeed + "\n" + ", airHumidity: " + airHumidity + ", pressure: " + pressure
                + "\n measurement date; " + measurementDate + "\n" + "_____________________________" + "\n";
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
        private LocalDateTime measurementDate;
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

        public SynopticMeasurementsBuilder measurementDate(LocalDateTime measurementDate) {
            this.measurementDate = measurementDate;
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

