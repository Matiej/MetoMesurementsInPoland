package pl.testaarosa.airmeasurements.model;

import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.time.LocalDateTime;

public class AirMeasurementFeDto {

    private int foreignId;
    private String cityName;
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

    public int getForeignId() {
        return foreignId;
    }

    public void setForeignId(int foreignId) {
        this.foreignId = foreignId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public LocalDateTime getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(LocalDateTime measurementDate) {
        this.measurementDate = measurementDate;
    }

    public LocalDateTime getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(LocalDateTime saveDate) {
        this.saveDate = saveDate;
    }

    public AirMeasurementLevel getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirMeasurementLevel airQuality) {
        this.airQuality = airQuality;
    }

    public String getStIndexLevel() {
        return stIndexLevel;
    }

    public void setStIndexLevel(String stIndexLevel) {
        this.stIndexLevel = stIndexLevel;
    }

    public String getSo2IndexLevel() {
        return so2IndexLevel;
    }

    public void setSo2IndexLevel(String so2IndexLevel) {
        this.so2IndexLevel = so2IndexLevel;
    }

    public String getNo2IndexLevel() {
        return no2IndexLevel;
    }

    public void setNo2IndexLevel(String no2IndexLevel) {
        this.no2IndexLevel = no2IndexLevel;
    }

    public String getCoIndexLevel() {
        return coIndexLevel;
    }

    public void setCoIndexLevel(String coIndexLevel) {
        this.coIndexLevel = coIndexLevel;
    }

    public String getPm10IndexLevel() {
        return pm10IndexLevel;
    }

    public void setPm10IndexLevel(String pm10IndexLevel) {
        this.pm10IndexLevel = pm10IndexLevel;
    }

    public String getPm25IndexLevel() {
        return pm25IndexLevel;
    }

    public void setPm25IndexLevel(String pm25IndexLevel) {
        this.pm25IndexLevel = pm25IndexLevel;
    }

    public String getO3IndexLevel() {
        return o3IndexLevel;
    }

    public void setO3IndexLevel(String o3IndexLevel) {
        this.o3IndexLevel = o3IndexLevel;
    }

    public String getC6h6IndexLevel() {
        return c6h6IndexLevel;
    }

    public void setC6h6IndexLevel(String c6h6IndexLevel) {
        this.c6h6IndexLevel = c6h6IndexLevel;
    }

    public MeasuringStation getMeasuringStation() {
        return measuringStation;
    }

    public void setMeasuringStation(MeasuringStation measuringStation) {
        this.measuringStation = measuringStation;
    }
}
