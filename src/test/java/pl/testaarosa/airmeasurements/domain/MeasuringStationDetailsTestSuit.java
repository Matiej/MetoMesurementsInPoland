package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDetailsRepository;

public class MeasuringStationDetailsTestSuit {

    private final MockMeasuringStationDetailsRepository detailsRepository = new MockMeasuringStationDetailsRepository();

    @Test
    public void testAirmeasurementsDto(){
        MeasuringStationDetails measuringStationDetails1 = detailsRepository.details1();
        MeasuringStationDetails measuringStationDetails2 = detailsRepository.details2();
        new EqualsTester().addEqualityGroup(measuringStationDetails1,measuringStationDetails2).testEquals();
    }
}
