package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

public class SynopticMeasurementDto {
    @JsonProperty("id_stacji")
    private int id;
    @JsonProperty("stacja")
    private String city;
    @JsonProperty("temperatura")
    private double temperature;
    @JsonProperty("predkosc_wiatru")
    private double windSpeed;
    @JsonProperty("wilgotnosc_wzgledna")
    private double airHumidity;
    @JsonProperty("cisnienie")
    private double pressure;
    @JsonProperty("data_pomiaru")
    private String measurementDate;
    @JsonProperty("godzina_pomiaru")
    private String getMeasurementHour;

    public SynopticMeasurementDto() {
    }

    public SynopticMeasurementDto(int id, String city, double temperature, double windSpeed, double airHumidity, double pressure) {
        this.id = id;
        this.city = city;
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.airHumidity = airHumidity;
        this.pressure = pressure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = Optional.ofNullable(id).orElse(9999);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = Optional.ofNullable(city).orElse("->No data available<-");
    }

    public double getTemperature() {
        return Optional.ofNullable(temperature).orElse(9999.0);
    }

    public void setTemperature(double temperature) {
        this.temperature = Optional.ofNullable(temperature).orElse(9999.0);
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = Optional.ofNullable(windSpeed).orElse(9999.0);
    }

    public double getAirHumidity() {
        return airHumidity;
    }

    public void setAirHumidity(double airHumidity) {
        this.airHumidity = Optional.ofNullable(airHumidity).orElse(9999.0);
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = Optional.ofNullable(pressure).orElse(9999.0);
    }

    public String getMeasurementDate() {
        return measurementDate;
    }

    public void setMeasurementDate(String measurementDate) {
        this.measurementDate = measurementDate;
    }

    public String getGetMeasurementHour() {
        return getMeasurementHour;
    }

    public void setGetMeasurementHour(String getMeasurementHour) {
        this.getMeasurementHour = getMeasurementHour;
    }

    @Override
    public String toString() {
        return "->>> Weather data for the city: \n" + "temperature: " + temperature + ", wind speed: " + windSpeed + ", air humidity: " + airHumidity + ", pressure: " + pressure + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SynopticMeasurementDto)) {
            return false;
        }
        SynopticMeasurementDto that = (SynopticMeasurementDto) o;
        return getId() == that.getId() && Double.compare(that.getTemperature(),
                getTemperature()) == 0 && Double.compare(that.getWindSpeed(),
                getWindSpeed()) == 0 && Double
                .compare(that.getAirHumidity(), getAirHumidity()) == 0 && Double.compare(that.getPressure(),
                getPressure()) == 0 && Objects.equals(
                getCity(),
                that.getCity());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCity(), getTemperature(), getWindSpeed(), getAirHumidity(), getPressure());
    }
}
