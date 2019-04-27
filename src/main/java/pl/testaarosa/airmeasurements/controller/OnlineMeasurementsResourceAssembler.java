package pl.testaarosa.airmeasurements.controller;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class OnlineMeasurementsResourceAssembler implements ResourceAssembler<OnlineMeasurementDto, Resource<OnlineMeasurementDto>> {


    public Resource<OnlineMeasurementDto> toResource(OnlineMeasurementDto onlineMeasurementDto, String value) {
        Resource rs = toResource(onlineMeasurementDto);
        switch (value) {
            case ("HOT"):
                rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getHottestOnlineMeasuringStation()).withSelfRel());
                break;
            case ("COLD"):
                rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getColdestOnlineMeasuringStation()).withSelfRel());
                break;
            default:
                System.out.println("need to add default. It will be something for just one measuerement by station ID");
        }
        rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getAllOnlineMeasuringStations()).withRel("allSt"));
        rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getGivenCityMeasuringOnlineStationsController(onlineMeasurementDto.getStationCity())).withRel("citySt"));
        rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getAllCites()).withRel("allCities"));
        return rs;
    }

    @Override
    public Resource<OnlineMeasurementDto> toResource(OnlineMeasurementDto onlineMeasurementDto) {
        Resource<OnlineMeasurementDto> rs = new Resource<>(onlineMeasurementDto);
        return rs;
    }
}
