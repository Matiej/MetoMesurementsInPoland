package pl.testaarosa.airmeasurements;

import pl.testaarosa.airmeasurements.domain.measurementsdto.AirMeasurementsDto;

public class App {
    public static void main(String[] args) {
        AirMeasurementsDto dto = new AirMeasurementsDto();
        dto.setStCalcDate(null);
        System.out.println(dto.getStCalcDate());
    }
}
