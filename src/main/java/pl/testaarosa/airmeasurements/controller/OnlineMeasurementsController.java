package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import pl.testaarosa.airmeasurements.domain.dtoFe.OnlineMeasurementDto;
import pl.testaarosa.airmeasurements.services.OnlineMeasurementService;

import java.util.NoSuchElementException;

@Api(description = "Online measurements directly from API")
@RestController
@RequestMapping("/online")
public class OnlineMeasurementsController {

    private final OnlineMeasurementService measuringOnlineServices;

    @Autowired
    public OnlineMeasurementsController(OnlineMeasurementService measuringOnlineServices) {
        this.measuringOnlineServices = measuringOnlineServices;
    }

    @ApiOperation(value = "Get all measuring stations localizations, address etc.", response = OnlineMeasurementDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Measuring stations found successful"),
            @ApiResponse(code = 400, message = "Can not find any online measuring stations!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 500, message = "External REST API server error! Can't get online measurements for all stations")})
    @RequestMapping(value = "/allSt", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllMeasuringStationsWithSynopticDataController() {
        try {
            return ResponseEntity.ok().body(measuringOnlineServices.getAllMeasuringStations());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find any online measuring stations");
        } catch (RestClientException e) {
            return ResponseEntity.status(500)
                    .body("External REST API server error! Can't get online measurements for all stations.-> ");
        }
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
    public ResponseEntity<Object> getGivenCityMeasuringStationsWithSynopticDataController(String city) {
        try {
            return ResponseEntity.status(200).body(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData(city));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Measuring stations for city " + city + " NOT found because of error-> "
                    + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("City name can't be empty!");

        } catch (RestClientException e) {
            return ResponseEntity.status(500).body("External REST API server error! Can't get online measurements for the city" + city + "," +
                    " -> ");
        }
    }

    @ApiOperation(value = "Get hottest current station", response = OnlineMeasurementDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Hottest measuring stations for given city found successful"),
            @ApiResponse(code = 400, message = "Can't find hottest online measurement!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! Can't get hottest measuring stations.")})
    @RequestMapping(value = "/hottest", method = RequestMethod.GET)
    public ResponseEntity<Object> getHottestOnlineStation() {
        try {
            return ResponseEntity.ok().body(measuringOnlineServices.getHottestOnlineStation());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find hottest online measurement!");
        } catch (RestClientException e) {
            return ResponseEntity.status(500).body("External REST API server error! Can't get hottest measurement online for station -> ");
        }
    }

    @ApiOperation(value = "Get coldest current station", response = OnlineMeasurementDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Coldest measuring stations for given city found"),
            @ApiResponse(code = 400, message = "Can't find coldest online measurement!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! Can't get coldest measuring stations.")})
        @RequestMapping(value = "/coldest", method = RequestMethod.GET)
    public ResponseEntity<Object> getColdestOnlineStation() {
        try {
            return ResponseEntity.ok(measuringOnlineServices.getColdestOnlineStation());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find coldest online measurement!");
        } catch (RestClientException e) {
            return ResponseEntity.status(500).body("External REST API server error! Can't get hottest measurement online for station -> ");
        }
    }
}
