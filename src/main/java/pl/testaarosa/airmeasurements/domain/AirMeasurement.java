package pl.testaarosa.airmeasurements.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class AirMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FOREIGN_ID")
    private int foreignId;
    @Column(name = "MEASUREMENT_DATE")
    private LocalDateTime measurementDate;
    @Column(name = "SAVE_DATE")
    private LocalDateTime saveDate;
    @Enumerated(value = EnumType.STRING)
    private AirMeasurementLevel airQuality;
    private String stIndexLevel; //powietrze ogólnie
    private String so2IndexLevel; //dwutlenek siarki
    private String no2IndexLevel; //dwutlenek azotu
    private String coIndexLevel;//tlenek wegla
    private String pm10IndexLevel; //pył zawieszony PM10
    private String pm25IndexLevel;
    private String o3IndexLevel;
    private String c6h6IndexLevel;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "STATION_ID")
    private MeasuringStation measuringStation;
    @ManyToOne
    @JoinColumn(name = "CITY_ID")
    private City city;

    public AirMeasurement() {
    }

    private AirMeasurement(AirMaesurementsBuilder builder) {
        this.foreignId = builder.foreignId;
        this.measurementDate = builder.measurementDate;
        this.saveDate = builder.saveDate;
        this.airQuality = builder.airQuality;
        this.stIndexLevel = builder.stIndexLevel;
        this.so2IndexLevel = builder.so2IndexLevel;
        this.no2IndexLevel = builder.no2IndexLevel;
        this.coIndexLevel = builder.coIndexLevel;
        this.pm10IndexLevel = builder.pm10IndexLevel;
        this.pm25IndexLevel = builder.pm25IndexLevel;
        this.o3IndexLevel = builder.o3IndexLevel;
        this.c6h6IndexLevel = builder.c6h6IndexLevel;
        this.measuringStation = builder.measuringStation;
        this.city = builder.city;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public int getForeignId() {
        return foreignId;
    }

    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public AirMeasurementLevel getAirQuality() {
        return airQuality;
    }

    public String getStIndexLevel() {
        return stIndexLevel;
    }

    public String getSo2IndexLevel() {
        return so2IndexLevel;
    }

    public String getNo2IndexLevel() {
        return no2IndexLevel;
    }

    public String getCoIndexLevel() {
        return coIndexLevel;
    }

    public String getPm10IndexLevel() {
        return pm10IndexLevel;
    }

    public String getPm25IndexLevel() {
        return pm25IndexLevel;
    }

    public String getO3IndexLevel() {
        return o3IndexLevel;
    }

    public String getC6h6IndexLevel() {
        return c6h6IndexLevel;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @JsonIgnore
    public MeasuringStation getMeasuringStation() {
        return measuringStation;
    }

    public void setMeasuringStation(MeasuringStation measuringStation) {
        this.measuringStation = measuringStation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AirMeasurement)) return false;
        AirMeasurement that = (AirMeasurement) o;
        return getForeignId() == that.getForeignId() &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getMeasurementDate(), that.getMeasurementDate()) &&
                Objects.equals(getSaveDate(), that.getSaveDate()) &&
                getAirQuality() == that.getAirQuality() &&
                Objects.equals(getStIndexLevel(), that.getStIndexLevel()) &&
                Objects.equals(getSo2IndexLevel(), that.getSo2IndexLevel()) &&
                Objects.equals(getNo2IndexLevel(), that.getNo2IndexLevel()) &&
                Objects.equals(getCoIndexLevel(), that.getCoIndexLevel()) &&
                Objects.equals(getPm10IndexLevel(), that.getPm10IndexLevel()) &&
                Objects.equals(getPm25IndexLevel(), that.getPm25IndexLevel()) &&
                Objects.equals(getO3IndexLevel(), that.getO3IndexLevel()) &&
                Objects.equals(getC6h6IndexLevel(), that.getC6h6IndexLevel()) &&
                Objects.equals(getMeasuringStation(), that.getMeasuringStation());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getForeignId(), getMeasurementDate(), getSaveDate(), getAirQuality(), getStIndexLevel(), getSo2IndexLevel(), getNo2IndexLevel(), getCoIndexLevel(), getPm10IndexLevel(), getPm25IndexLevel(), getO3IndexLevel(), getC6h6IndexLevel(), getMeasuringStation());
    }

    @Override
    public String toString() {
        return "AirMeasurement: " + "id: " + id + ", Station id: " + foreignId + ", measurementDate: " + measurementDate + "\n, saveDate: " + saveDate + ", airQuality: " + airQuality + "\n" + ", stIndexLevel: " + stIndexLevel + ", so2IndexLevel: " + so2IndexLevel + "\n" + ", no2IndexLevel: " + no2IndexLevel + ", coIndexLevel: " + coIndexLevel + "\n" + ", pm10IndexLevel: " + pm10IndexLevel + ", pm25IndexLevel: " + pm25IndexLevel + "\n" + ", o3IndexLevel: " + o3IndexLevel + ", c6h6IndexLevel: " + c6h6IndexLevel + "\n" + "_____________________________" + "\n";
    }


    public static class AirMaesurementsBuilder {
        private Long id;
        private int foreignId;
        private LocalDateTime measurementDate;
        private LocalDateTime saveDate;
        private AirMeasurementLevel airQuality;
        private String stIndexLevel; //powietrze ogólnie
        private String so2IndexLevel; //dwutlenek siarki
        private String no2IndexLevel; //dwutlenek azotu
        private String coIndexLevel;//tlenek wegla
        private String pm10IndexLevel; //pył zawieszony PM10
        private String pm25IndexLevel;
        private String o3IndexLevel;
        private String c6h6IndexLevel;
        private MeasuringStation measuringStation;
        private City city;

        public AirMaesurementsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public AirMaesurementsBuilder foreignId(int foreignId) {
            this.foreignId = foreignId;
            return this;
        }

        public AirMaesurementsBuilder measurementDate(LocalDateTime measurementDate) {
            this.measurementDate = measurementDate;
            return this;
        }

        public AirMaesurementsBuilder saveDate(LocalDateTime saveDate) {
            this.saveDate = saveDate;
            return this;
        }

        public AirMaesurementsBuilder airQuality(AirMeasurementLevel airQuality) {
            this.airQuality = airQuality;
            return this;
        }

        public AirMaesurementsBuilder stIndexLevel(String stIndexLevel) {
            this.stIndexLevel = stIndexLevel;
            return this;
        }

        public AirMaesurementsBuilder so2IndexLevel(String so2IndexLevel) {
            this.so2IndexLevel = so2IndexLevel;
            return this;
        }

        public AirMaesurementsBuilder no2IndexLevel(String no2IndexLevel) {
            this.no2IndexLevel = no2IndexLevel;
            return this;
        }

        public AirMaesurementsBuilder coIndexLevel(String coIndexLevel) {
            this.coIndexLevel = coIndexLevel;
            return this;
        }

        public AirMaesurementsBuilder pm10IndexLevel(String pm10IndexLevel) {
            this.pm10IndexLevel = pm10IndexLevel;
            return this;
        }

        public AirMaesurementsBuilder pm25IndexLevel(String pm25IndexLevel) {
            this.pm25IndexLevel = pm25IndexLevel;
            return this;
        }

        public AirMaesurementsBuilder o3IndexLevel(String o3IndexLevel) {
            this.o3IndexLevel = o3IndexLevel;
            return this;
        }

        public AirMaesurementsBuilder c6h6IndexLevel(String c6h6IndexLevel) {
            this.c6h6IndexLevel = c6h6IndexLevel;
            return this;
        }

        public AirMaesurementsBuilder measuringStation(MeasuringStation measuringStation) {
            this.measuringStation = measuringStation;
            return this;
        }

        public AirMaesurementsBuilder city(City city) {
            this.city=city;
            return this;
        }

        public AirMeasurement build() {
            return new AirMeasurement(this);
        }
    }
}
