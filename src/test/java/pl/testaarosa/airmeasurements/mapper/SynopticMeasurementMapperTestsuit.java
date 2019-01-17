package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockSynopticDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SynopticMeasurementMapperTestsuit {
    private final MockSynopticDtoRepository dtoRepository = new MockSynopticDtoRepository();
    private final SynopticMeasurementMapper mapper = new SynopticMeasurementMapper();
    private final MockSynopticRepository synopticRepository = new MockSynopticRepository();

    @Test
    public void shouldMaptToSynopticMeasurement(){
        //given
        SynopticMeasurementDto mockRepo = dtoRepository.mockSynopticDtoRepositories().get(1);
        SynopticMeasurement expect = synopticRepository.synopticMeasurementsOrderColdest().get(1);
        //when
        SynopticMeasurement result = mapper.maptToSynopticMeasurement(mockRepo);
        result.setSaveDate(expect.getSaveDate());
        assertEquals(expect,result);
    }

    @Test
    public void shouldMaptToSynopticMeasurement2(){
        //given
        SynopticMeasurementDto mockRepo = dtoRepository.mockSynopticDtoRepositories().get(3);
        SynopticMeasurement expect = synopticRepository.synopticMeasurementsOrderColdest().get(3);
        //when
        SynopticMeasurement result = mapper.maptToSynopticMeasurement(mockRepo);
        result.setSaveDate(expect.getSaveDate());
        assertEquals(expect,result);
    }

    @Test
    public void shouldNotMaptToSynopticMeasurement(){
        //given
        SynopticMeasurementDto mockRepo = dtoRepository.mockSynopticDtoRepositories().get(1);
        SynopticMeasurement expect = synopticRepository.synopticMeasurementsOrderColdest().get(3);
        //when
        SynopticMeasurement result = mapper.maptToSynopticMeasurement(mockRepo);
        result.setSaveDate(expect.getSaveDate());
        assertNotEquals(expect,result);
    }
}
