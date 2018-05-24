package pl.testaarosa.airmeasurements.domain;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationDetailsRepository;

public class MeasuringStationDetailsTestSuit {

    private final MockMeasuringStationDetailsRepository detailsRepository = new MockMeasuringStationDetailsRepository();

    @Test
    public void testAirmeasurementsDto(){
        MeasuringStationDetails measuringStationDetails1 = detailsRepository.detailsList().get(0);
        MeasuringStationDetails measuringStationDetails2 = detailsRepository.detailsList().get(1);
        new EqualsTester().addEqualityGroup(measuringStationDetails1,measuringStationDetails2).testEquals();
    }
}
