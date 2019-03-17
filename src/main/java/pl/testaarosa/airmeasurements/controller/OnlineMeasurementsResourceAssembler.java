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
        if (value.equals("HOT")) {
            rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getHottestOnlineMeasuringStation()).withSelfRel());
        } else {
            rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getColdestOnlineMeasuringStation()).withSelfRel());
        }
        rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getAllOnlineMeasuringStationsController()).withRel("allSt"));
        rs.add(linkTo(methodOn(OnlineMeasurementsController.class).getGivenCityMeasuringOnlineStationsController(onlineMeasurementDto.getStationCity())).withRel("citySt"));
        return rs;
}

    @Override
    public Resource<OnlineMeasurementDto> toResource(OnlineMeasurementDto onlineMeasurementDto) {
        Resource<OnlineMeasurementDto> rs = new Resource<>(onlineMeasurementDto);
        return rs;
    }
}
