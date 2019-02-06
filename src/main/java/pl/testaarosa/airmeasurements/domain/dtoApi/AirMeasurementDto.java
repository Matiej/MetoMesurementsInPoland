package pl.testaarosa.airmeasurements.domain.dtoApi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class AirMeasurementDto {

    private int id;
    private String stCalcDate;
    private LevelDto stIndexLevel; //powietrze ogólnie
    private LevelDto so2IndexLevel; //dwutlenek siarki
    private String so2SourceDataDate;
    private LevelDto no2IndexLevel; //dwutlenek azotu
    private LevelDto coIndexLevel;//tlenek wegla
    private String coSourceDataDate;
    private LevelDto pm10IndexLevel; //pył zawieszony PM10
    private LevelDto pm25IndexLevel;
    private LevelDto o3IndexLevel;
    private LevelDto c6h6IndexLevel;

    @Override
    public String toString() {
        return "Date: " + stCalcDate + " ** Air measurements: \n" + "Air dust level: " + stCalcDate + "\n" + "  SO2 dwutlentek siarki level: " + so2IndexLevel + "\n" + "  NO2 dwutletek azotu level: " + no2IndexLevel + "\n" + "  O3 ozon level: " + o3IndexLevel + "\n" + "  CO tlenek wegla level: " + coIndexLevel + "\n" + "  PM10 pyl zawieszony level: " + pm10IndexLevel + "\n" + "  PM2.5 pyl zawieszony level: " + pm25IndexLevel + "\n";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
//        this.id = Optional.ofNullable(id).orElse(999);
        this.id = id;
    }

    public String getStCalcDate() {
        return stCalcDate;
    }

    public void setStCalcDate(String stCalcDate) {
        this.stCalcDate = Optional.ofNullable(stCalcDate).orElse(currentData());
    }

    public LevelDto getStIndexLevel() {
        return stIndexLevel;
    }

    public void setStIndexLevel(LevelDto stIndexLevel) {
        this.stIndexLevel = Optional.ofNullable(stIndexLevel).orElse(emptyObj());
    }

    public LevelDto getSo2IndexLevel() {
        return so2IndexLevel;
    }

    public void setSo2IndexLevel(LevelDto so2IndexLevel) {
        this.so2IndexLevel = Optional.ofNullable(so2IndexLevel).orElse(emptyObj());
    }

    public String getSo2SourceDataDate() {
        return so2SourceDataDate;
    }

    public void setSo2SourceDataDate(String so2SourceDataDate) {
        this.so2SourceDataDate = Optional.ofNullable(so2SourceDataDate).orElse(currentData());
    }

    public LevelDto getNo2IndexLevel() {
        return no2IndexLevel;
    }

    public void setNo2IndexLevel(LevelDto no2IndexLevel) {
        this.no2IndexLevel = Optional.ofNullable(no2IndexLevel).orElse(emptyObj());
    }

    public LevelDto getCoIndexLevel() {
        return coIndexLevel;
    }

    public void setCoIndexLevel(LevelDto coIndexLevel) {
        this.coIndexLevel = Optional.ofNullable(coIndexLevel).orElse(emptyObj());
    }

    public String getCoSourceDataDate() {
        return coSourceDataDate;
    }

    public void setCoSourceDataDate(String coSourceDataDate) {
        this.coSourceDataDate = Optional.ofNullable(coSourceDataDate).orElse(currentData());
    }

    public LevelDto getPm10IndexLevel() {
        return pm10IndexLevel;
    }

    public void setPm10IndexLevel(LevelDto pm10IndexLevel) {
        this.pm10IndexLevel = Optional.ofNullable(pm10IndexLevel).orElse(emptyObj());
    }

    public LevelDto getPm25IndexLevel() {
        return pm25IndexLevel;
    }

    public void setPm25IndexLevel(LevelDto pm25IndexLevel) {
        this.pm25IndexLevel = Optional.ofNullable(pm25IndexLevel).orElse(emptyObj());
    }

    public LevelDto getO3IndexLevel() {
        return o3IndexLevel;
    }

    public void setO3IndexLevel(LevelDto o3IndexLevel) {
        this.o3IndexLevel = Optional.ofNullable(o3IndexLevel).orElse(emptyObj());
    }

    public LevelDto getC6h6IndexLevel() {
        return c6h6IndexLevel;
    }

    public void setC6h6IndexLevel(LevelDto c6h6IndexLevel) {
        this.c6h6IndexLevel = Optional.ofNullable(c6h6IndexLevel).orElse(emptyObj());
    }

    private LevelDto emptyObj() {
        return (new LevelDto.Builder().id(6).indexLevelName("N/A").build());
    }

    private String currentData() {
        Date current = new Date();
        SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fd.format(current);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AirMeasurementDto)) {
            return false;
        }
        AirMeasurementDto that = (AirMeasurementDto) o;
        return getId() == that.getId() && Objects.equals(getStCalcDate(), that.getStCalcDate()) && Objects.equals(
                getStIndexLevel(),
                that.getStIndexLevel()) && Objects.equals(getSo2IndexLevel(),
                that.getSo2IndexLevel()) && Objects.equals(
                getSo2SourceDataDate(),
                that.getSo2SourceDataDate()) && Objects.equals(getNo2IndexLevel(),
                that.getNo2IndexLevel()) && Objects.equals(
                getCoIndexLevel(),
                that.getCoIndexLevel()) && Objects.equals(getCoSourceDataDate(),
                that.getCoSourceDataDate()) && Objects.equals(
                getPm10IndexLevel(),
                that.getPm10IndexLevel()) && Objects.equals(getPm25IndexLevel(),
                that.getPm25IndexLevel()) && Objects.equals(getO3IndexLevel(),
                that.getO3IndexLevel()) && Objects
                .equals(getC6h6IndexLevel(), that.getC6h6IndexLevel());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(),
                getStCalcDate(),
                getStIndexLevel(),
                getSo2IndexLevel(),
                getSo2SourceDataDate(),
                getNo2IndexLevel(),
                getCoIndexLevel(),
                getCoSourceDataDate(),
                getPm10IndexLevel(),
                getPm25IndexLevel(),
                getO3IndexLevel(),
                getC6h6IndexLevel());
    }
}
