package pl.testaarosa.airmeasurements.domain.dtoApi;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LevelDtoTestSuit {

    @Test
    public void testCityRegionDto(){
        //given
        LevelDto levelDto1 = new LevelDto.Builder()
                .indexLevelName("index")
                .id(1)
                .build();

        LevelDto levelDto2 = new LevelDto.Builder()
                .indexLevelName("index")
                .id(1)
                .build();
        LevelDto levelDto3 = new LevelDto.Builder()
                .indexLevelName("indexWrong Obj")
                .id(3)
                .build();
        //when
        //then
        assertNotNull(levelDto1);
        new EqualsTester().addEqualityGroup(levelDto1,levelDto2).testEquals();
        assertEquals(levelDto1,levelDto2);
        assertNotEquals(levelDto1,levelDto3);

    }
}
