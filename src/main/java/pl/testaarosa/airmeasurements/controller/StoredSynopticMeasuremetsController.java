package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.services.StoredSynopticMeasurementService;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

@Api(description = "Get synoptic measurements from database")
@RestController
@RequestMapping("/storedSynoptic")
public class StoredSynopticMeasuremetsController {

    private final StoredSynopticMeasurementService storedSynopticMeasurementService;

    @Autowired
    public StoredSynopticMeasuremetsController(StoredSynopticMeasurementService storedSynopticMeasurementService) {
        this.storedSynopticMeasurementService = storedSynopticMeasurementService;
    }

    @ApiOperation(value = "Get all stored synoptic measurements for all available cities", response = MeasuringStation.class,
            position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurement information."),
            @ApiResponse(code = 200, message = "Synoptic measurements for all cities loaded from db successful."),
            @ApiResponse(code = 400, message = "No synoptic measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!")})
    @RequestMapping(value = "/allMeasurements", method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(storedSynopticMeasurementService.findAll());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No synoptics measurements found!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).body("Server error. Can't get synoptics measurement information.");
        }
    }

    @ApiOperation(value = "Get synoptic measurements for given date", response = SynopticMeasurement.class)
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD",
            dataType = "String", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get synoptic measurements information."),
            @ApiResponse(code = 200, message = "Synoptic measurements for given date loaded from db successful."),
            @ApiResponse(code = 400, message = "No measurements for given date found."),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data or data format!")})
    @RequestMapping(value = "/byDate", method = RequestMethod.GET)
    public ResponseEntity<Object> findAllSynopticMeasurementsByDate(String date) {
        try {
            return ResponseEntity.ok(storedSynopticMeasurementService.getSynopticMeasuremets(date));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body("No measurements for date: " + date + " found.");
        } catch (DateTimeException e) {
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect data or data format for input: " + date);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(503).body("Data base server error. Can't get air synoptic information.");
        }
    }

    @ApiOperation(value = "Get top hottest measurements and places", response = SynopticMeasurement.class)
    @ApiImplicitParam(required = true, defaultValue = "10", dataType = "String", name = "noOfResults",
            value = "Number of results", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurements information."),
            @ApiResponse(code = 200, message = "Hottest top measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No hottest measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data format!")})
    @RequestMapping(value = "/hottestTop", method = RequestMethod.GET)
    public ResponseEntity<Object> findHottestPlaces(String noOfResults) {
        try {
            return ResponseEntity.ok(storedSynopticMeasurementService.getHottestPlaces(noOfResults));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No hottest measurements found!");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(503).body("Data base server error. Can't get synoptic measurements information.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("Not Acceptable! Wrong number format");
        }
    }

    @ApiOperation(value = "Get top coldest measurements and places", response = SynopticMeasurement.class)
    @ApiImplicitParam(required = true, defaultValue = "10", dataType = "String", name = "noOfResults",
            value = "Number of results", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurements information."),
            @ApiResponse(code = 200, message = "Coldest top measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No coldest measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data format!")})
    @RequestMapping(value = "/coldestTop", method = RequestMethod.GET)
    public ResponseEntity<Object> findColdestPlaces(String noOfResults) {
        try {
            return ResponseEntity.ok(storedSynopticMeasurementService.getColdestPlaces(noOfResults));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No hottest measurements found!");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(503).body("Data base server error. Can't get synoptic measurements information.");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.status(406).body("Not Acceptable! Wrong number format");
        }
    }
}
