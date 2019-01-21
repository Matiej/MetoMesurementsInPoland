package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDetailsRepository;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertNotSame;

public class MeasuringStationDetailsTestSuit {

    private MockMeasuringStationDetailsRepository detailsRepository;

    @Before
    public void init() {
        detailsRepository = new MockMeasuringStationDetailsRepository();
    }

    @Test
    public void testAirmeasurementsDto(){
        //given
        MeasuringStationDetails measuringStationDetails1 = detailsRepository.detailsList().get(0);
        MeasuringStationDetails measuringStationDetails2 = detailsRepository.detailsList().get(0);
        MeasuringStationDetails measuringStationDetails3 = detailsRepository.detailsList().get(1);
        //when
        //then
        assertNotNull(measuringStationDetails1);
        new EqualsTester().addEqualityGroup(measuringStationDetails1,measuringStationDetails2).testEquals();
        assertNotSame(measuringStationDetails1, measuringStationDetails3);
    }
}
