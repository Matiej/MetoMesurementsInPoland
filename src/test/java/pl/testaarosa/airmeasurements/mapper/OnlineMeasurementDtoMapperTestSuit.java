package pl.testaarosa.airmeasurements.mapper;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.repositories.*;

import java.util.*;

import static org.junit.Assert.*;

public class OnlineMeasurementDtoMapperTestSuit {
    private final MockOnlineRepository mockOnlineRepo = new MockOnlineRepository();
    private final OnlineMeasurementMapper mapper = new OnlineMeasurementMapper();
    private final MockStationRepository mockStationRepository = new MockStationRepository();
    private final MockAirRepository mockAirRepository = new MockAirRepository();
    private final MockSynopticRepository synopticRepository = new MockSynopticRepository();

    @Test
    public void shouldMapToMeasuringStationColdMeasurements() {
        //given
        OnlineMeasurementDto expect = mockOnlineRepo.measuringStationOnLineList().get(0);
        MeasuringStation measuringStation = mockStationRepository.stations().get(0);
        AirMeasurement airMeasurement = mockAirRepository.airMeasurements1().get(0);
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
        MeasuringStation measuringStation = mockStationRepository.stations().get(3);
        AirMeasurement airMeasurement = mockAirRepository.airMeasurements2().get(2);
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
        MeasuringStation measuringStation = mockStationRepository.stations().get(3);
        AirMeasurement airMeasurement = mockAirRepository.airMeasurements2().get(1);
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
        List<MeasuringStation> mSt = mockStationRepository.stations();
        List<AirMeasurement> aMs1 = mockAirRepository.airMeasurements1();
        List<AirMeasurement> aMs2 = mockAirRepository.airMeasurements2();
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
        synopticMeasurementMap.put(sMsc.get(0).getCity(), sMsc.get(0));
        synopticMeasurementMap.put(sMsc.get(1).getCity(), sMsc.get(0));
        synopticMeasurementMap.put(sMsh.get(2).getCity(), sMsh.get(2));
        synopticMeasurementMap.put(sMsh.get(3).getCity(), sMsh.get(3));
        return synopticMeasurementMap;
    }

}
