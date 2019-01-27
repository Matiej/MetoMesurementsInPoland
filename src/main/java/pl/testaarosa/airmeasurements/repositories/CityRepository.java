package pl.testaarosa.airmeasurements.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.testaarosa.airmeasurements.domain.City;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Override
    List<City> findAll();

    @Override
    City save(City city);

    boolean existsAllByCityName(String cityName);

    City findOneByCityName(String cityName);

}
