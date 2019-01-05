package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

public class LevelDtoTestSuit {

    @Test
    public void testCityRegionDto(){
        LevelDto levelDto1 = new LevelDto.Builder()
                .indexLevelName("index")
                .id(1)
                .build();

        LevelDto levelDto2 = new LevelDto.Builder()
                .indexLevelName("index")
                .id(1)
                .build();
        new EqualsTester().addEqualityGroup(levelDto1,levelDto2).testEquals();
    }
}
