package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.services.AddMeasurementsService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@Api(description = "Add measurements to database")
@RestController
@RequestMapping("/add")
public class AddMeasurementsController {

    private final AddMeasurementsService measurementsService;

    @Autowired
    public AddMeasurementsController(AddMeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @Transactional
    @RequestMapping(value = "/station", method = RequestMethod.GET)
    @ApiOperation(value = "Add measurements from selected station", response = MeasuringStation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "No measuring station found"),
            @ApiResponse(code = 200, message = "Measurement saved successful"),
            @ApiResponse(code = 400, message = "No measuring station for given ID")})
    @ApiImplicitParam(required = true, name = "id", value = "station Id", paramType = "query")
    public ResponseEntity<Object> addMeasurements(Integer id) {
        try {
            return ResponseEntity.ok(measurementsService.addOne(id));
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't find measuring station id: " + id);
        }
    }

    @Transactional
    @ApiOperation(value = "Add all measurements from API.", response = MeasuringStation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Can't add measurements."),
            @ApiResponse(code = 200, message = "Measurements for all stations saved successful."),
            @ApiResponse(code = 400, message = "Measurements for all stations wasn't saved.")})
    @RequestMapping(value = "/station/all", method = RequestMethod.GET)
    public ResponseEntity<Object> allMeasurements() {
        try {
            return ResponseEntity.ok(measurementsService.addMeasurementsAllStations());
        } catch (ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't add measurements for all stations");
        } catch (InterruptedException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Can't add measurements for all stations");
        }
    }
}
