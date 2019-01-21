package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class MeasuringStationTestSuit {

    private MockMeasuringStationRepository mockMeasuringStationRepository;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
    }

    @Test
    public void testAirmeasurementsDto(){
        //given
        MeasuringStation station1 = mockMeasuringStationRepository.stations().get(0);
        MeasuringStation station2 = mockMeasuringStationRepository.stations().get(1);
        MeasuringStation station3 = mockMeasuringStationRepository.stations().get(3);
        //when
        //then
        assertNotNull(station1);
        new EqualsTester().addEqualityGroup(station1, station2).testEquals();
        assertNotSame(station1,station3);
    }
}
