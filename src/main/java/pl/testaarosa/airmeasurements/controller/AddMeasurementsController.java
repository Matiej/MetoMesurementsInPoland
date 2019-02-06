package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.*;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.services.AddMeasurementsService;

import java.util.NoSuchElementException;

@Api(description = "Add measurements to database")
@RestController
@RequestMapping("/add")
public class AddMeasurementsController {

    private final AddMeasurementsService measurementsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AddMeasurementsController.class);

    @Autowired
    public AddMeasurementsController(AddMeasurementsService measurementsService) {
        this.measurementsService = measurementsService;
    }

    @RequestMapping(value = "/oneSt", method = RequestMethod.POST)
    @ApiOperation(value = "Add measurements from selected station", response = MeasuringStation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Measurement saved successful"),
            @ApiResponse(code = 400, message = "No measuring station found for given ID"),
            @ApiResponse(code = 404, message = "Server has not found anything matching the requested URI! No measuring station found for given ID"),
            @ApiResponse(code = 406, message = "Not Acceptable! Station ID must be an INTEGER and can not be empty!"),
            @ApiResponse(code = 500, message = "External REST API server error. Can't add measurement for given stationId!"),
            @ApiResponse(code = 503, message = "Data base server error. Can't add  measurement to data base."),})
    @ApiImplicitParam(required = true, name = "stationId", value = "Measuring station Id", dataType = "int", paramType = "query",
            defaultValue = "114")
    public ResponseEntity<Object> addOneMeasurement(Integer stationId) {
        try {
            return ResponseEntity.status(201).body(measurementsService.addOneStationMeasurement(stationId));
        } catch (NoSuchElementException e) {
            LOGGER.error(e.getMessage(),e);
            return ResponseEntity.status(400).body("Can't find measuring station ID: " + stationId);
        } catch (NumberFormatException | MethodArgumentTypeMismatchException e) {
            LOGGER.error(e.getMessage(),e);
            return ResponseEntity.status(406).body("Not Acceptable! Givens Station ID -> " + stationId +
                    " must be an INTEGER and can not be empty!");
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(),e);
            return ResponseEntity.status(500).body("External REST API server error. Can't add measurement for: " + stationId +" stationId!");
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage(),e);
            return ResponseEntity.status(503).body("Data base server error. Can't add measurement to data base for station:  " + stationId);
        }
    }

    @ApiOperation(value = "Add all measurements from API for all stations.", response = MeasuringStation.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Measurements for all stations saved successful."),
            @ApiResponse(code = 404, message = "Server has not found antything matching the requested URI! No measuring station found for given ID"),
            @ApiResponse(code = 500, message = "External REST API server error. Can't add measurements"),
            @ApiResponse(code = 503, message = "Data base server error. Can't add measurements.")})
    @RequestMapping(value = "/allSt", method = RequestMethod.POST)
    public ResponseEntity<Object> allMeasurements() {
        try {
            return ResponseEntity.status(201).body(measurementsService.addMeasurementsAllStations());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(500).body("External REST API server error. Can't add measurements");
        } catch (HibernateException e) {
            LOGGER.error(e.getMessage(), e);
            return ResponseEntity.status(503).body("Data base server error. Can't add measurement to data base!");
        }
    }
}
