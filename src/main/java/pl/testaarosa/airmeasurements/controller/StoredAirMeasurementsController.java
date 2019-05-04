package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.AirMeasurementLevel;
import pl.testaarosa.airmeasurements.services.StoredAirMeasurementsService;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

@Api(description = "Get measurements from database")
@RestController
@RequestMapping("/stored/air")
public class StoredAirMeasurementsController {

    private final StoredAirMeasurementsService storedAirMeasurementsService;

    @Autowired
    public StoredAirMeasurementsController(StoredAirMeasurementsService storedAirMeasurementsService) {
        this.storedAirMeasurementsService = storedAirMeasurementsService;
    }

    @ApiOperation(value = "Get air measurements by Air Quality", response = AirMeasurement.class, position = 2)
    @ApiImplicitParam(required = true, name = "airLevel", value = "Choose Air Quality", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get air measurements information."),
            @ApiResponse(code = 200, message = "Air measurements for all given air level loaded from db successful."),
            @ApiResponse(code = 400, message = "No air measurements found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect air level type or wrong enum!")})
    @RequestMapping(value = "/airMeasurementsQy", method = RequestMethod.GET)
    public ResponseEntity<Object> findPlaceByAirQuality(AirMeasurementLevel airLevel) {
//            return ResponseEntity.ok(storedMeasurementsService.getAirMeasurementsByLevel(airLevel));
        return ResponseEntity.ok(storedAirMeasurementsService.getAirMeasurementsByLevel(airLevel));
    }

    @ApiOperation(value = "Get air measurements for given date", response = AirMeasurement.class, position = 3)
    @ApiImplicitParam(required = true, name = "date", dataType = "String", value = "Date in format: YYYY-MM-DD",
            paramType = "query", example = "2015-05-21")
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get air measurements information."),
            @ApiResponse(code = 200, message = "Air measurements for given date loaded from db successful."),
            @ApiResponse(code = 400, message = "No measurements for given date found."),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measurements found!"),
            @ApiResponse(code = 406, message = "Not Acceptable! Incorrect data or data format!")})
    @RequestMapping(value = "/airMeasurementsDate", method = RequestMethod.GET)
    public ResponseEntity<Object> findAllAirMeasurementsByDate(String date) {
        try {
            return ResponseEntity.ok(storedAirMeasurementsService.getAirMeasurementsByDate(date));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(400).body("No measurements for date: " + date + " found.");
        } catch (DateTimeException e) {
            return ResponseEntity.status(406).body("Not Acceptable! Incorrect data or data format for input: " + date);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(503).body("Data base server error. Can't get air measurements information.");
        }
    }
}
