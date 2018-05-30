package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.domain.measurementsdto.SynopticMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockSynopticDtoRepository;
import pl.testaarosa.airmeasurements.repositories.MockSynopticRepository;

import static org.junit.Assert.assertEquals;

public class SynopticMeasurementMapperTestsuit {
    private final MockSynopticDtoRepository dtoRepository = new MockSynopticDtoRepository();
    private final SynopticMeasurementMapper mapper = new SynopticMeasurementMapper();
    private final MockSynopticRepository synopticRepository = new MockSynopticRepository();

    @Test
    public void testMaptToSynopticMeasurement(){
        SynopticMeasurementDto synopticMeasurementDto = dtoRepository.mockSynopticDtoRepositories().get(1);
        SynopticMeasurements result = mapper.maptToSynopticMeasurement(synopticMeasurementDto);
        SynopticMeasurements expect = synopticRepository.synopticMeasurements1().get(1);
        assertEquals(expect,result);
    }
}
