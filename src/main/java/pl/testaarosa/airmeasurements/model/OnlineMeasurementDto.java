package pl.testaarosa.airmeasurements.model;

import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.util.Objects;

public class OnlineMeasurementDto {

    private int id;
    private final double gegrLatitude;
    private final double gegrLongitude;
    private final String stationName;
    private final String stationStreet;
    private final String stationCity;
    private final String stationDistrict;
    private final String stationVoivodeship;
    private final AirMeasurement airMs;
    private final SynopticMeasurement synopticMs;

    private OnlineMeasurementDto(OnlineMeasurementBuilder builder) {
        this.id = builder.id;
        this.gegrLatitude = builder.gegrLatitude;
        this.gegrLongitude = builder.gegrLongitude;
        this.stationName = builder.stationName;
        this.stationStreet = builder.stationStreet;
        this.stationCity = builder.stationCity;
        this.stationDistrict = builder.stationDistrict;
        this.stationVoivodeship = builder.stationVoivodeship;
        this.airMs = builder.airMs;
        this.synopticMs = builder.synopticMs;
    }

    public int getId() {
        return id;
    }

    public double getGegrLatitude() {
        return gegrLatitude;
    }

    public double getGegrLongitude() {
        return gegrLongitude;
    }

    public String getStationName() {
        return stationName;
    }

    public String getStationStreet() {
        return stationStreet;
    }

    public String getStationCity() {
        return stationCity;
    }

    public String getStationDistrict() {
        return stationDistrict;
    }

    public String getStationVoivodeship() {
        return stationVoivodeship;
    }

    public AirMeasurement getAirMs() {
        return airMs;
    }

    public SynopticMeasurement getSynopticMs() {
        return synopticMs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnlineMeasurementDto that = (OnlineMeasurementDto) o;
        return id == that.id &&
                Double.compare(that.gegrLatitude, gegrLatitude) == 0 &&
                Double.compare(that.gegrLongitude, gegrLongitude) == 0 &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(stationStreet, that.stationStreet) &&
                Objects.equals(stationCity, that.stationCity) &&
                Objects.equals(stationDistrict, that.stationDistrict) &&
                Objects.equals(stationVoivodeship, that.stationVoivodeship) &&
                Objects.equals(airMs, that.airMs) &&
                Objects.equals(synopticMs, that.synopticMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gegrLatitude, gegrLongitude, stationName, stationStreet, stationCity, stationDistrict, stationVoivodeship, airMs, synopticMs);
    }

    @Override
    public String toString() {
        return "Measuring station id: " + id + ", stationName: " + stationName + "\n" + "geographic latitude: " + gegrLatitude + ", geographic longitude: " + gegrLongitude + "\n" + "station street: " + stationStreet + ", station city: " + stationCity + "\n" + "station district: " + stationDistrict + ", station voivodeship: " + stationVoivodeship + "\n" + synopticMs + "\n" + airMs + "\n";
    }

    public static class OnlineMeasurementBuilder {
        private int id;
        private double gegrLatitude;
        private double gegrLongitude;
        private String stationName;
        private String stationStreet;
        private String stationCity;
        private String stationDistrict;
        private String stationVoivodeship;

        private AirMeasurement airMs;

        private SynopticMeasurement synopticMs;

        public OnlineMeasurementBuilder id(int id) {
            this.id = id;
            return this;
        }

        public OnlineMeasurementBuilder gegrLatitude(double gegrLatitude) {
            this.gegrLatitude = gegrLatitude;
            return this;
        }

        public OnlineMeasurementBuilder gegrLongitude(double gegrLongitude) {
            this.gegrLongitude = gegrLongitude;
            return this;
        }

        public OnlineMeasurementBuilder stationName(String stationName) {
            this.stationName = stationName;
            return this;
        }

        public OnlineMeasurementBuilder stationStreet(String stationStreet) {
            this.stationStreet = stationStreet;
            return this;
        }

        public OnlineMeasurementBuilder stationCity(String stationCity) {
            this.stationCity = stationCity;
            return this;
        }

        public OnlineMeasurementBuilder stationDistrict(String stationDistrict) {
            this.stationDistrict = stationDistrict;
            return this;
        }

        public OnlineMeasurementBuilder stationVoivodeship(String stationVoivodeship) {
            this.stationVoivodeship = stationVoivodeship;
            return this;
        }

        public OnlineMeasurementBuilder airMs(AirMeasurement airMs) {
            this.airMs = airMs;
            return this;
        }

        public OnlineMeasurementBuilder synopticMs(SynopticMeasurement synopticMs) {
            this.synopticMs = synopticMs;
            return this;
        }

        public OnlineMeasurementDto build() {
            return new OnlineMeasurementDto(this);
        }
    }
}
