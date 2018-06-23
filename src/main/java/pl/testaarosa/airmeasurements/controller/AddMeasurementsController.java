package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.services.AddMeasurementsService;

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

    @RequestMapping(value = "/station", method = RequestMethod.GET)
    @ApiOperation(value = "Add measurements from selected station")
    @ApiImplicitParam(required = true, name = "id", value = "station Id", dataType = "int", paramType = "query")
    public String addMeasurements(int id) throws ExecutionException, InterruptedException {
        return measurementsService.addMeasurements(id);
    }

    @ApiOperation(value = "Add all measurements from API. Warning! It takes about 90 minutes")
    @RequestMapping(value = "/station/all", method = RequestMethod.GET)
    public String allMeasurements() throws ExecutionException, InterruptedException {
        return measurementsService.addMeasurementsAllStations();
    }
}
