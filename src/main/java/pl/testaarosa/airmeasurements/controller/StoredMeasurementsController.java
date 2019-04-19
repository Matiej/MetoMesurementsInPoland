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
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.services.StoredAirMeasurementsService;
import pl.testaarosa.airmeasurements.services.StoredMeasurementsService;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

@Api(description = "Get measurements from database")
@RestController
@RequestMapping("/stored/station")
public class StoredMeasurementsController {

    private final StoredMeasurementsService storedMeasurementsService;
    private final StoredAirMeasurementsService storedAirMeasurementsService;

    @Autowired
    public StoredMeasurementsController(StoredMeasurementsService storedMeasurementsService, StoredAirMeasurementsService storedAirMeasurementsService) {
        this.storedMeasurementsService = storedMeasurementsService;
        this.storedAirMeasurementsService = storedAirMeasurementsService;
    }

    @ApiOperation(value = "Get all stored synoptic & air measurements for all stations", response = MeasuringStation.class,
    position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 503, message = "Server error. Can't get measurement stations information."),
            @ApiResponse(code = 200, message = "Measurements for all stations loaded from db successful."),
            @ApiResponse(code = 400, message = "No measuring stations found!"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring stations found!")})
    @RequestMapping(value = "/allMeasurements", method = RequestMethod.GET)
    public ResponseEntity<Object> findAll() {
        try {
            return ResponseEntity.ok(storedMeasurementsService.findAll());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("No measuring stations found!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return ResponseEntity.status(503).body("Server error. Can't get measurement stations information.");
        }
    }
}
