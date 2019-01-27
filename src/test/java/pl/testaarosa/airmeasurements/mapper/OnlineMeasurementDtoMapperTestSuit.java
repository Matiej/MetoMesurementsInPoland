package pl.testaarosa.airmeasurements.mapper;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.*;

import java.util.*;

import static org.junit.Assert.*;

public class OnlineMeasurementDtoMapperTestSuit {
    private MockOnlineMeasurementRepository mockOnlineRepo;
    private OnlineMeasurementMapper mapper;
    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockAirMeasurementRepository mockAirMeasurementRepository;
    private MockSynopticMeasurementRepository synopticRepository;

    @Before
    public void init() {
        mockOnlineRepo = new MockOnlineMeasurementRepository();
        mapper = new OnlineMeasurementMapper();
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockAirMeasurementRepository = new MockAirMeasurementRepository();
        synopticRepository = new MockSynopticMeasurementRepository();

    }

    @Test
    public void shouldMapToMeasuringStationColdMeasurements() {
        //given
        OnlineMeasurementDto expect = mockOnlineRepo.measuringStationOnLineList().get(0);
        MeasuringStation measuringStation = mockMeasuringStationRepository.stations().get(0);
        AirMeasurement airMeasurement = mockAirMeasurementRepository.airMeasurements1().get(0);
        SynopticMeasurement synopticMeasurement = synopticRepository.synopticMeasurementsOrderColdest().get(0);
        //when
        OnlineMeasurementDto result = mapper.mapToOnlineMeasurementDto(measuringStation, airMeasurement, synopticMeasurement);
        assertNotNull(expect);
        assertNotNull(result);
        assertEquals(expect, result);

    }

    @Test
    public void shouldMapToMeasuringStationHotMeasurements() {
        //given
        OnlineMeasurementDto expect = mockOnlineRepo.measuringStationOnLineList().get(3);
        MeasuringStation measuringStation = mockMeasuringStationRepository.stations().get(3);
        AirMeasurement airMeasurement = mockAirMeasurementRepository.airMeasurements2().get(2);
        SynopticMeasurement synopticMeasurement = synopticRepository.synopticMeasurementsOrderHottest().get(3);
        //when
        OnlineMeasurementDto result = mapper.mapToOnlineMeasurementDto(measuringStation, airMeasurement, synopticMeasurement);
        assertNotNull(expect);
        assertNotNull(result);
        assertEquals(expect, result);

    }

    @Test
    public void shouldNotMapCorrectToMeasuringStation() {
        //given
        OnlineMeasurementDto expect = mockOnlineRepo.measuringStationOnLineList().get(3);
        MeasuringStation measuringStation = mockMeasuringStationRepository.stations().get(3);
        AirMeasurement airMeasurement = mockAirMeasurementRepository.airMeasurements2().get(1);
        SynopticMeasurement synopticMeasurement = synopticRepository.synopticMeasurementsOrderHottest().get(3);
        //when
        OnlineMeasurementDto result = mapper.mapToOnlineMeasurementDto(measuringStation, airMeasurement, synopticMeasurement);
        assertNotNull(expect);
        assertNotNull(result);
        assertNotSame(expect, result);

    }

    @Test
    public void shouldMapToMeasuringStationList() {
        //given
        List<OnlineMeasurementDto> expect = mockOnlineRepo.measuringStationOnLineList();
        LinkedHashMap<MeasuringStation, AirMeasurement> measurementMap = getMeasuringStationAirMeasurementLinkedHashMap();
        LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap = getStringSynopticMeasurementLinkedHashMap();
        //when
        expect.remove(0);
        List<OnlineMeasurementDto> result = mapper.mapToOnlneMsDtoList(measurementMap, synopticMeasurementMap);
        //then
        assertNotNull(expect);
        assertNotNull(result);
        assertEquals(expect, result);

    }

    @NotNull
    private LinkedHashMap<MeasuringStation, AirMeasurement> getMeasuringStationAirMeasurementLinkedHashMap() {
        List<MeasuringStation> mSt = mockMeasuringStationRepository.stations();
        List<AirMeasurement> aMs1 = mockAirMeasurementRepository.airMeasurements1();
        List<AirMeasurement> aMs2 = mockAirMeasurementRepository.airMeasurements2();
        LinkedHashMap<MeasuringStation, AirMeasurement> measurementMap = new LinkedHashMap<>();
        measurementMap.put(mSt.get(0), aMs1.get(0));
        measurementMap.put(mSt.get(1), aMs1.get(0));
        measurementMap.put(mSt.get(2), aMs2.get(2));
        measurementMap.put(mSt.get(3), aMs2.get(2));
        return measurementMap;
    }

    @NotNull
    private LinkedHashMap<String, SynopticMeasurement> getStringSynopticMeasurementLinkedHashMap() {
        List<SynopticMeasurement> sMsc = synopticRepository.synopticMeasurementsOrderColdest();
        List<SynopticMeasurement> sMsh = synopticRepository.synopticMeasurementsOrderHottest();
        LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap = new LinkedHashMap<>();
        synopticMeasurementMap.put(sMsc.get(0).getCityName(), sMsc.get(0));
        synopticMeasurementMap.put(sMsc.get(1).getCityName(), sMsc.get(0));
        synopticMeasurementMap.put(sMsh.get(2).getCityName(), sMsh.get(2));
        synopticMeasurementMap.put(sMsh.get(3).getCityName(), sMsh.get(3));
        return synopticMeasurementMap;
    }

}
