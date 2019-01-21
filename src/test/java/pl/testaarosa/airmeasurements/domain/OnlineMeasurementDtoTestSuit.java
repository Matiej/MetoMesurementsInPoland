package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.MockOnlineMeasurementRepository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class OnlineMeasurementDtoTestSuit {
    private MockOnlineMeasurementRepository mockOnlineMeasurementRepository;

    @Before
    public void init() {
        mockOnlineMeasurementRepository = new MockOnlineMeasurementRepository();
    }

    @Test
    public void testAirmeasurementsDto(){
        //given
        OnlineMeasurementDto onlineMeasurementDto1 = mockOnlineMeasurementRepository.measuringStationOnLineList().get(0);
        OnlineMeasurementDto onlineMeasurementDto2 = mockOnlineMeasurementRepository.measuringStationOnLineList().get(1);
        OnlineMeasurementDto onlineMeasurementDto3 = mockOnlineMeasurementRepository.measuringStationOnLineList().get(2);
        //when
        //then
        assertNotNull(onlineMeasurementDto1);
        new EqualsTester().addEqualityGroup(onlineMeasurementDto1, onlineMeasurementDto2).testEquals();
        assertEquals(onlineMeasurementDto1, onlineMeasurementDto2);
        assertNotEquals(onlineMeasurementDto1,onlineMeasurementDto3);
    }
}
