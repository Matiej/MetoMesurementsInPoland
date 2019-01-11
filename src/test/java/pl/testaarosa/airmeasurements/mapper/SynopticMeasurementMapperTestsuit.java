package pl.testaarosa.airmeasurements.mapper;

import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoApi.SynopticMeasurementDto;
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
        SynopticMeasurement result = mapper.maptToSynopticMeasurement(synopticMeasurementDto);
        SynopticMeasurement expect = synopticRepository.synopticMeasurementsOrderColdest().get(1);
        result.setSaveDate(expect.getSaveDate());
        assertEquals(expect,result);
    }
}
