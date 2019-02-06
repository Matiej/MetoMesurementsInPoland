package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

@JsonClassDescription(value = "city")
public class CityDto {
    private int id;
    @JsonProperty("name")
    private String cityName;
    @JsonProperty("commune")
    private CityRegionDto cityRegionDto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = Optional.ofNullable(id).orElse(9999);
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = Optional.ofNullable(cityName).orElse("N/A");
    }

    public CityRegionDto getCityRegionDto() {
        return cityRegionDto;
    }

    public void setCityRegionDto(CityRegionDto cityRegionDto) {
        this.cityRegionDto = Optional.ofNullable(cityRegionDto).orElse(optionalCityRegionDto());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityDto)) return false;
        CityDto cityDto = (CityDto) o;
        return getId() == cityDto.getId() &&
                Objects.equals(getCityName(), cityDto.getCityName()) &&
                Objects.equals(getCityRegionDto(), cityDto.getCityRegionDto());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCityName(), getCityRegionDto());
    }

    @Override
    public String toString() {
        return "*CityDto: \n id= " + id + ", cityName= " + cityName + "\n" + cityRegionDto;
    }

    private CityRegionDto optionalCityRegionDto() {
        final String noData = "no data";
        CityRegionDto cityRegionDto = new CityRegionDto();
        cityRegionDto.setCommuneName(noData);
        cityRegionDto.setDistrictName(noData);
        cityRegionDto.setVoivodeship(noData);
        return  cityRegionDto;
    }
}
