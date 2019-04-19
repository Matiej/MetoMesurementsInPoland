package pl.testaarosa.airmeasurements.repositories;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.domain.QAirMeasurement;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AirMeasurementDaoImpl implements AirMeasurementDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public AirMeasurement save(final AirMeasurement airMeasurement) {
        em.persist(airMeasurement);
        return airMeasurement;
    }

    @Override
    public List<AirMeasurement> findAllAirMeasurementsByAirQuality(AirMeasurementLevel airMeasurementLevel) {
        JPAQuery<AirMeasurement> query = new JPAQuery<>(em);
        QAirMeasurement qAirMeasurement = QAirMeasurement.airMeasurement;
        List<AirMeasurement> airMeasurementList = query.from(qAirMeasurement)
                .where(qAirMeasurement.airQuality.eq(airMeasurementLevel))
                .orderBy(qAirMeasurement.measurementDate.desc())
                .fetch();
        return airMeasurementList;
    }

    @Override
    public List<AirMeasurement> getAirMeasurementsBySaveDate(LocalDateTime saveDate) {
        JPAQuery<AirMeasurement> query = new JPAQuery<>(em);
        QAirMeasurement qAirMeasurement = QAirMeasurement.airMeasurement;
        List<AirMeasurement> airMeasurementJPAQuery = query.from(qAirMeasurement)
                .where(qAirMeasurement.saveDate.after(saveDate)
                        .and(qAirMeasurement.saveDate.before(saveDate.plusHours(23).plusMinutes(59).plusSeconds(59)))).fetch();
        return airMeasurementJPAQuery;
    }
}
