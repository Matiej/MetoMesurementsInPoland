package pl.testaarosa.airmeasurements.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "STATION_DETAILS")
public class MeasuringStationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String commune;
    private String district;
    private String voivodeship;

    public MeasuringStationDetails() {
    }

    private MeasuringStationDetails(MeasuringStationDetailsBuilder builder) {
        this.city = builder.city;
        this.commune = builder.commune;
        this.district = builder.district;
        this.voivodeship = builder.voivodeship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasuringStationDetails)) return false;
        MeasuringStationDetails details = (MeasuringStationDetails) o;
        return Objects.equals(getId(), details.getId()) &&
                Objects.equals(getCity(), details.getCity()) &&
                Objects.equals(getCommune(), details.getCommune()) &&
                Objects.equals(getDistrict(), details.getDistrict()) &&
                Objects.equals(getVoivodeship(), details.getVoivodeship());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCity(), getCommune(), getDistrict(), getVoivodeship());
    }

    @Override
    public String toString() {
        return "MeasuringStationDetails{" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", commune='" + commune + '\'' +
                ", district='" + district + '\'' +
                ", voivodeship='" + voivodeship + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCommune() {
        return commune;
    }

    public String getDistrict() {
        return district;
    }

    public String getVoivodeship() {
        return voivodeship;
    }

    public static class MeasuringStationDetailsBuilder {
        private Long id;
        private String city;
        private String commune;
        private String district;
        private String voivodeship;

        public MeasuringStationDetailsBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MeasuringStationDetailsBuilder city(String city) {
            this.city = city;
            return this;
        }

        public MeasuringStationDetailsBuilder commune(String commune) {
            this.commune = commune;
            return this;
        }

        public MeasuringStationDetailsBuilder district(String district) {
            this.district = district;
            return this;
        }

        public MeasuringStationDetailsBuilder voivodeship(String voivodeship) {
            this.voivodeship = voivodeship;
            return this;
        }

        public MeasuringStationDetails build() {
            return new MeasuringStationDetails(this);
        }
    }
}
