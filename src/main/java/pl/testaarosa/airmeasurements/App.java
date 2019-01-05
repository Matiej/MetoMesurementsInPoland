package pl.testaarosa.airmeasurements;

import pl.testaarosa.airmeasurements.domain.dtoApi.AirMeasurementDto;

public class App {
    public static void main(String[] args) {
        AirMeasurementDto dto = new AirMeasurementDto();
        dto.setStCalcDate(null);
        System.out.println(dto.getStCalcDate());
    }
}
