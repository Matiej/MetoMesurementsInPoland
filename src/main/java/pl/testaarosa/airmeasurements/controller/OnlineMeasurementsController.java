package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.model.CityFeDto;
import pl.testaarosa.airmeasurements.model.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.services.OnlineMeasurementService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Api(description = "Online measurements directly from API")
@RestController
@RequestMapping("/online")
@CrossOrigin(origins = {"http://localhost:4200", "http://0.0.0.0:4200", "http://192.168.1.101:4200", "http://192.168.1.101:4200"})
public class OnlineMeasurementsController {

    private final OnlineMeasurementService measuringOnlineServices;
    private final OnlineMeasurementsResourceAssembler assembler;
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineMeasurementsController.class);

    @Autowired
    public OnlineMeasurementsController(OnlineMeasurementService measuringOnlineServices, OnlineMeasurementsResourceAssembler assembler) {
        this.measuringOnlineServices = measuringOnlineServices;
        this.assembler = assembler;
    }

    @ApiOperation(value = "Get all measuring stations localizations, address etc.", response = OnlineMeasurementDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Measuring stations found successful"),
            @ApiResponse(code = 400, message = "Can not find any online measuring stations!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 500, message = "External REST API server error! Can't get online measurements for all stations")})
    @RequestMapping(value = "/allSt", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllOnlineMeasuringStations() {
        List<Resource<OnlineMeasurementDto>> allMeasuringStations = measuringOnlineServices.getAllMeasuringStations().stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(new Resources<>(allMeasuringStations,
                linkTo(methodOn(OnlineMeasurementsController.class).getAllOnlineMeasuringStations()).withSelfRel(),
        linkTo(methodOn(OnlineMeasurementsController.class).getHottestOnlineMeasuringStation()).withRel("hottest"),
        linkTo(methodOn(OnlineMeasurementsController.class).getColdestOnlineMeasuringStation()).withRel("coldest")));
    }

    @ApiOperation(value = "Get all online measurements for given city name", response = OnlineMeasurementDto.class)
    @ApiImplicitParam(required = true, name = "city", value = "City name", dataType = "string", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Measuring stations for given city found successful"),
            @ApiResponse(code = 400, message = "Can't get measuring stations for given city."),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI!  Measuring stations for given city NOT found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! City name for measurement stations can not be empty!"),
            @ApiResponse(code = 500, message = "External REST API server error! Can't get online measurements for given station.")})
    @RequestMapping(value = "/citySt", method = RequestMethod.GET)
    public ResponseEntity<Object> getGivenCityMeasuringOnlineStationsController(String city) {
        List<Resource<OnlineMeasurementDto>> givenCityMeasuringStationsWithSynopticData = measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData(city).stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.status(200).body(new Resources<>(givenCityMeasuringStationsWithSynopticData,
                linkTo(methodOn(OnlineMeasurementsController.class).getGivenCityMeasuringOnlineStationsController(city)).withSelfRel(),
                linkTo(methodOn(OnlineMeasurementsController.class).getAllOnlineMeasuringStations()).withRel("allSt"),
                linkTo(methodOn(OnlineMeasurementsController.class).getHottestOnlineMeasuringStation()).withRel("hottest"),
                linkTo(methodOn(OnlineMeasurementsController.class).getColdestOnlineMeasuringStation()).withRel("coldest")));
    }

    @ApiOperation(value = "Get hottest current station", response = OnlineMeasurementDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Hottest measuring stations for given city found successful"),
            @ApiResponse(code = 400, message = "Can't find hottest online measurement!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! Can't get hottest measuring stations.")})
    @RequestMapping(value = "/hottest", method = RequestMethod.GET)
    public ResponseEntity<?> getHottestOnlineMeasuringStation() {
        OnlineMeasurementDto hottestOnlineStation = measuringOnlineServices.getHottestOnlineStation();
        Resource<OnlineMeasurementDto> rs = assembler.toResource(hottestOnlineStation, "HOT");
        return ResponseEntity.ok(rs);
//        return ResponseEntity.ok().body(hottestOnlineStation);
    }

    @ApiOperation(value = "Get coldest current station", response = OnlineMeasurementDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Coldest measuring stations for given city found"),
            @ApiResponse(code = 400, message = "Can't find coldest online measurement!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! Can't get coldest measuring stations.")})
    @RequestMapping(value = "/coldest", method = RequestMethod.GET)
    public ResponseEntity<Object> getColdestOnlineMeasuringStation() {
        OnlineMeasurementDto hottestOnlineStation = measuringOnlineServices.getColdestOnlineStation();
        Resource rs = assembler.toResource(hottestOnlineStation, "COLD");
        return ResponseEntity.ok(rs);
    }

    @ApiOperation(value = "Get measurements for all cities.", response = CityFeDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Online measurements for cities found successful"),
            @ApiResponse(code = 400, message = "Can not find any online measurements"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 500, message = "External REST API server error! Can't get online measurements for all cities")})
    @RequestMapping(value = "/allCities", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllCites() {
        return ResponseEntity.ok().body(measuringOnlineServices.onlineMeasurementsForCities());
    }
}

