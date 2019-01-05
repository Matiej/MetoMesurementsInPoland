package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MeasuringStationDto {
    private int id;
    private String stationName;
    private String gegrLat;
    private String gegrLon;
    private String addressStreet;
    @JsonProperty("city")
    private CityDto cityDto;

    public MeasuringStationDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getGegrLat() {
        return gegrLat;
    }

    public void setGegrLat(String gegrLat) {
        this.gegrLat = gegrLat;
    }

    public String getGegrLon() {
        return gegrLon;
    }

    public void setGegrLon(String gegrLon) {
        this.gegrLon = gegrLon;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    public CityDto getCityDto() {
        return cityDto;
    }

    public void setCityDto(CityDto cityDto) {
        this.cityDto = cityDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasuringStationDto)) return false;
        MeasuringStationDto that = (MeasuringStationDto) o;
        return getId() == that.getId() &&
                Objects.equals(getStationName(), that.getStationName()) &&
                Objects.equals(getGegrLat(), that.getGegrLat()) &&
                Objects.equals(getGegrLon(), that.getGegrLon()) &&
                Objects.equals(getAddressStreet(), that.getAddressStreet()) &&
                Objects.equals(getCityDto(), that.getCityDto());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getStationName(), getGegrLat(), getGegrLon(), getAddressStreet(), getCityDto());
    }

    @Override
    public String toString() {
        return "MeasuringStationDto: " + "id: " + id + "\n" + "  stationName: " + stationName + "\n" + "  geographic latitude: " + gegrLat + '\'' + "  geographic longitude: " + gegrLon + "\n" + "  addressStreet: " + addressStreet + "\n" + cityDto + "\n";
    }
}

