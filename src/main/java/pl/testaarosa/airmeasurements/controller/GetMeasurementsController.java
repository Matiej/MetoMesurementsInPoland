package pl.testaarosa.airmeasurements.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.testaarosa.airmeasurements.domain.*;
import pl.testaarosa.airmeasurements.services.GetMeasurementsService;
import pl.testaarosa.airmeasurements.domain.AirMeasurements;
import pl.testaarosa.airmeasurements.domain.MeasurementsAirLevel;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurements;

import java.util.List;

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
    @RequestMapping(value = "/stations/all", method = RequestMethod.GET)
    public List<MeasuringStation> findAll() {
        return getMeasurementsService.findAll();
    }

    @ApiOperation(value = "Get air measurements from given date")
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD", paramType = "query")
    @RequestMapping(value = "/measurements/date", method = RequestMethod.GET)
    public List<AirMeasurements> findAllAirMeasurementsByDate(String date) {
        return getMeasurementsService.getAirMeasurements(date);
    }

    @ApiOperation(value = "Get places by Air Quality")
    @ApiImplicitParam(required = true, name = "airLevel", value = "Choose Air Quality", paramType = "query")
    @RequestMapping(value = "/measurements/air", method = RequestMethod.GET)
    public List<AirMeasurements> findPlaceByAirQuality(MeasurementsAirLevel airLevel) {
        return getMeasurementsService.getAirMeasurements(airLevel);
    }

    @ApiOperation(value = "Get synoptic measurements from given date")
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD", paramType = "query")
    @RequestMapping(value = "/measurements/synoptic", method = RequestMethod.GET)
    public List<SynopticMeasurements> findAllSynopticMeasurementsByDate(String date) {
        return getMeasurementsService.getSynopticMeasuremets(date);
    }

    @ApiOperation(value = "Get coldest place from given date")
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD", paramType = "query")
    @RequestMapping(value = "/measurements/coldest", method = RequestMethod.GET)
    public SynopticMeasurements findColdestPlaceByDate(String date) {
        return getMeasurementsService.getColdestPlaceGivenDate(date);
    }

    @ApiOperation(value = "Get coldest measurements")
    @RequestMapping(value = "/measurements/coldestTop", method = RequestMethod.GET)
    public List<SynopticMeasurements> findColdestPlaces() {
        return getMeasurementsService.getColdestPlaces();
    }

    @ApiOperation(value = "Get hottest place from given date")
    @ApiImplicitParam(required = true, name = "date", value = "Date in format: YYYY-MM-DD", paramType = "query")
    @RequestMapping(value = "/measurements/hottest", method = RequestMethod.GET)
    @ResponseStatus()
    public SynopticMeasurements findAHottestPlaceByDate(String date) {
        return getMeasurementsService.getHottestPlaceGivenDate(date);
    }

    @ApiOperation(value = "Get hottest measurements")
    @RequestMapping(value = "/measurements/hottestTop", method = RequestMethod.GET)
    public List<SynopticMeasurements> findHottestPlaces() {
        return getMeasurementsService.getHottestPlaces();
    }
}
