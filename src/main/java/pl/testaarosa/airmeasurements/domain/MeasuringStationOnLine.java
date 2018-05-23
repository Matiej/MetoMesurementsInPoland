package pl.testaarosa.airmeasurements.domain;

import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;

import java.util.Objects;

public class MeasuringStationOnLine {

    private final double gegrLatitude;
    private final double gegrLongitude;
    private final String stationName;
    private final String stationStreet;
    private final String stationCity;
    private final String stationDistrict;
    private final String stationVoivodeship;
    private final AirMeasurementsDto air;
    private final SynopticMeasurementDto synoptics;
    private int id;

    private MeasuringStationOnLine(MeasuringStationOnLineBuilder builder) {
        this.id = builder.id;
        this.gegrLatitude = builder.gegrLatitude;
        this.gegrLongitude = builder.gegrLongitude;
        this.stationName = builder.stationName;
        this.stationStreet = builder.stationStreet;
        this.stationCity = builder.stationCity;
        this.stationDistrict = builder.stationDistrict;
        this.stationVoivodeship = builder.stationVoivodeship;
        this.air = builder.air;
        this.synoptics = builder.synoptics;
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

    public AirMeasurementsDto getAir() {
        return air;
    }

    public SynopticMeasurementDto getSynoptics() {
        return synoptics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasuringStationOnLine)) {
            return false;
        }
        MeasuringStationOnLine that = (MeasuringStationOnLine) o;
        return getId() == that.getId() && Double.compare(that.getGegrLatitude(),
                                                         getGegrLatitude()) == 0 && Double.compare(that.getGegrLongitude(),
                                                                                                   getGegrLongitude()) == 0 && Objects
                .equals(getStationName(), that.getStationName()) && Objects.equals(getStationStreet(),
                                                                                   that.getStationStreet()) && Objects.equals(
                getStationCity(),
                that.getStationCity()) && Objects.equals(getStationDistrict(),
                                                         that.getStationDistrict()) && Objects.equals(
                getStationVoivodeship(),
                that.getStationVoivodeship()) && Objects.equals(getAir(),
                                                                that.getAir()) && Objects.equals(getSynoptics(),
                                                                                                 that.getSynoptics());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(),
                            getGegrLatitude(),
                            getGegrLongitude(),
                            getStationName(),
                            getStationStreet(),
                            getStationCity(),
                            getStationDistrict(),
                            getStationVoivodeship(),
                            getAir(),
                            getSynoptics());
    }

    @Override
    public String toString() {
        return "Measuring station id: " + id + ", stationName: " + stationName + "\n" + "geographic latitude: " + gegrLatitude + ", geographic longitude: " + gegrLongitude + "\n" + "station street: " + stationStreet + ", station city: " + stationCity + "\n" + "station district: " + stationDistrict + ", station voivodeship: " + stationVoivodeship + "\n" + synoptics + "\n" + air + "\n";
    }

    public static class MeasuringStationOnLineBuilder {
        private int id;
        private double gegrLatitude;
        private double gegrLongitude;
        private String stationName;
        private String stationStreet;
        private String stationCity;
        private String stationDistrict;
        private String stationVoivodeship;
        private AirMeasurementsDto air;
        private SynopticMeasurementDto synoptics;

        public MeasuringStationOnLineBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MeasuringStationOnLineBuilder gegrLatitude(double gegrLatitude) {
            this.gegrLatitude = gegrLatitude;
            return this;
        }

        public MeasuringStationOnLineBuilder gegrLongitude(double gegrLongitude) {
            this.gegrLongitude = gegrLongitude;
            return this;
        }

        public MeasuringStationOnLineBuilder stationName(String stationName) {
            this.stationName = stationName;
            return this;
        }

        public MeasuringStationOnLineBuilder stationStreet(String stationStreet) {
            this.stationStreet = stationStreet;
            return this;
        }

        public MeasuringStationOnLineBuilder stationCity(String stationCity) {
            this.stationCity = stationCity;
            return this;
        }

        public MeasuringStationOnLineBuilder stationDistrict(String stationDistrict) {
            this.stationDistrict = stationDistrict;
            return this;
        }

        public MeasuringStationOnLineBuilder stationVoivodeship(String stationVoivodeship) {
            this.stationVoivodeship = stationVoivodeship;
            return this;
        }

        public MeasuringStationOnLineBuilder air(AirMeasurementsDto air) {
            this.air = air;
            return this;
        }

        public MeasuringStationOnLineBuilder synoptics(SynopticMeasurementDto synoptics) {
            this.synoptics = synoptics;
            return this;
        }

        public MeasuringStationOnLine build() {
            return new MeasuringStationOnLine(this);
        }
    }
}
