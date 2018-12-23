package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;
import pl.testaarosa.airmeasurements.services.GetMeasurementsService;

import java.time.DateTimeException;
import java.util.List;
import java.util.NoSuchElementException;

@Api(description = "Get measurements from database")
@RestController
@RequestMapping("/get")
public class GetMeasurementsController {

    private final GetMeasurementsService getMeasurementsService;

    @Autowired
    public GetMeasurementsController(GetMeasurementsService getMeasurementsService) {
        this.getMeasurementsService = getMeasurementsService;
    }

    @ApiOperation(value = "Get all stations")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No measuring stations found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!")})
    @RequestMapping(value = "/stations/all", method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(getMeasurementsService.findAll());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No measuring stations found!");
        }
    }

    @ApiOperation(value = "Get places by Air Quality", response = AirMeasurements.class)
    @ApiImplicitParam(required = true, name = "airLevel", value = "Choose Air Quality", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get air measurements information."),
            @ApiResponse(code = 200, message = "Air measurements for all given air level loaded from db successful."),
            @ApiResponse(code = 400, message = "No air measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect air level type or wrong enum!")})
    @RequestMapping(value = "/measurements/air", method = RequestMethod.GET)
    public ResponseEntity<Object> findPlaceByAirQuality(MeasurementsAirLevel airLevel) {
        try {
            return ResponseEntity.ok(getMeasurementsService.getAirMeasurements(airLevel));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect air level type or wrong enum like: " + airLevel);
        } catch (NoSuchElementException | ConversionFailedException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No air measurements found for given air level: " + airLevel);
        }
    }

    @ApiOperation(value = "Get air measurements for given date", response = AirMeasurements.class)
    @ApiImplicitParam(required = true, name = "date", dataType = "String", value = "Date in format: YYYY-MM-DD",
            paramType = "query", example = "2015-05-21")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get air measurements information."),
            @ApiResponse(code = 200, message = "Air measurements for given date loaded from db successful."),
            @ApiResponse(code = 400, message = "No measurements for given date found."),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data or data format!")})
    @RequestMapping(value = "/measurements/date", method = RequestMethod.GET)
    public ResponseEntity<Object> findAllAirMeasurementsByDate(String date) {
        try {
            return ResponseEntity.ok(getMeasurementsService.getAirMeasurements(date));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body("No measurements for date: " + date + " found.");
        } catch (DateTimeException e) {
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect data or data format for input: " + date);
        }
    }

    @ApiOperation(value = "Get synoptic measurements for given date")
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD",
            dataType = "String", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get synoptic measurements information."),
            @ApiResponse(code = 200, message = "Synoptic measurements for given date loaded from db successful."),
            @ApiResponse(code = 400, message = "No measurements for given date found."),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data or data format!")})
    @RequestMapping(value = "/measurements/synoptic", method = RequestMethod.GET)
    public ResponseEntity<Object> findAllSynopticMeasurementsByDate(String date) {
        try {
            return ResponseEntity.ok(getMeasurementsService.getSynopticMeasuremets(date));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body("No measurements for date: " + date + " found.");
        } catch (DateTimeException e) {
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect data or data format for input: " + date);
        }
    }

    @ApiOperation(value = "Get coldest place for given date")
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD", dataType = "String",
            paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get synoptic measurements information."),
            @ApiResponse(code = 200, message = "Synoptic coldest measurement for given date loaded from db successful."),
            @ApiResponse(code = 400, message = "No measurements for given date found."),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data or data format!")})
    @RequestMapping(value = "/measurements/coldest", method = RequestMethod.GET)
    public ResponseEntity<Object> findColdestPlaceByDate(String date) {
        try {
            return ResponseEntity.ok(getMeasurementsService.getColdestPlaceGivenDate(date));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No synoptic measurements for date: " + date + " found. ");
        } catch (DateTimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect data or data format for input: " + date);
        }
    }

    @ApiOperation(value = "Get 10 coldest measurements", response = SynopticMeasurements.class)
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurements information."),
            @ApiResponse(code = 200, message = "Coldest top measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No coldest measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!")})
    @RequestMapping(value = "/measurements/coldestTop", method = RequestMethod.GET)
    public ResponseEntity<Object> findColdestPlaces() {
        try {
            return ResponseEntity.ok(getMeasurementsService.getColdestPlaces());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No coldest measurements found!");
        }
    }

    @ApiOperation(value = "Get hottest place for given date", response = SynopticMeasurements.class)
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD", dataType = "String",
            paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurements information."),
            @ApiResponse(code = 200, message = "Hottest top measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No hottest measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data or data format!")})
    @RequestMapping(value = "/measurements/hottest", method = RequestMethod.GET)
    public ResponseEntity<Object> findAHottestPlaceByDate(String date) {
        try {
            return ResponseEntity.ok().body(getMeasurementsService.getHottestPlaceGivenDate(date));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No hottest measurement found!");
        } catch (DateTimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect data or data format for input: " + date);
        }
    }

    @ApiOperation(value = "Get 10 hottest measurements", response = SynopticMeasurements.class)
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurements information."),
            @ApiResponse(code = 200, message = "Hottest top measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No hottest measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!")})
    @RequestMapping(value = "/measurements/hottestTop", method = RequestMethod.GET)
    public ResponseEntity<Object> findHottestPlaces() {
        try {
            return ResponseEntity.ok(getMeasurementsService.getHottestPlaces());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No coldest measurements found!");
        }
    }
}
