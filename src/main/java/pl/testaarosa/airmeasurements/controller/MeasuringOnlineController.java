package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.MeasuringStationOnLine;
import pl.testaarosa.airmeasurements.services.MeasuringOnlineServices;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@Api(description = "Get online measurements directly from API")
@RestController
@RequestMapping("/online")
public class MeasuringOnlineController {

    private final MeasuringOnlineServices measuringOnlineServices;

    @Autowired
    public MeasuringOnlineController(MeasuringOnlineServices measuringOnlineServices) {
        this.measuringOnlineServices = measuringOnlineServices;
    }

    @ApiOperation(value = "Get all measuring stations localizations, address etc.", response = MeasuringStationOnLine.class)
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get online measuring stations information."),
            @ApiResponse(code = 200, message = "Measuring stations found successful"),
            @ApiResponse(code = 400, message = "Can not find any online measuring stations!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!")})
    @RequestMapping(value = "/stations/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAllMeasuringStationsWithSynopticDataController() {
        try {
            return ResponseEntity.ok().body(measuringOnlineServices.getAllMeasuringStations());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Can't get measuring stations information because of exception-> " + e.getMessage());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find any online measuring stations");
        }
    }

    @ApiOperation(value = "Get all measurements stations for given city name", response = MeasuringStationOnLine.class)
    @ApiImplicitParam(required = true, name = "city", value = "City name", dataType = "string", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Measuring stations for given city found successful"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI!  Measuring stations for given city NOT found!"),
            @ApiResponse(code = 400, message = "Can't get measuring stations for given city."),
            @ApiResponse(code = 406, message = "Not Acceptable! City name for measurement stations can not be empty!")})
    @RequestMapping(value = "/stations/select", method = RequestMethod.GET)
    public ResponseEntity<Object> getGivenCityMeasuringStationsWithSynopticDataController(String city) {
        try {
            return ResponseEntity.status(200).body(measuringOnlineServices.getGivenCityMeasuringStationsWithSynopticData(city));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Can't get measuring stations form the city" + city + "," +
                    " because of exception-> " + e.getMessage());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Measuring stations for city " + city + " NOT found because of error-> "
                    + e.getMessage());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("City name can't be empty!");
        }
    }

    @ApiOperation(value = "Get hottest station", response = MeasuringStationOnLine.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Hottest measuring stations for given city found successful"),
            @ApiResponse(code = 400, message = "Can't find hottest online measurement!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! Can't get hottest measuring stations.")})
    @RequestMapping(value = "/stations/hottest", method = RequestMethod.GET)
    public ResponseEntity<Object> getHottestOnlineStation() {
        try {
            return ResponseEntity.ok().body(measuringOnlineServices.getHottestOnlineStation());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Can't get Hottest measuring station because of exception-> " + e.getMessage());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find hottest online measurement!");
        }
    }

    @ApiOperation(value = "Get coldest station", response = MeasuringStationOnLine.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "External server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Coldest measuring stations for given city found"),
            @ApiResponse(code = 400, message = "Can't find coldest online measurement!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! Can't get coldest measuring stations.")})
    @RequestMapping(value = "/stations/coldest", method = RequestMethod.GET)
    public ResponseEntity<Object> getColdestOnlineStation() {
        try {
            return ResponseEntity.ok(measuringOnlineServices.getColdestOnlineStation());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Can't get Coldest measuring station because of exception-> " + e.getMessage());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find coldest online measurement!");
        }
    }
}

