package pl.testaarosa.airmeasurements.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.testaarosa.airmeasurements.services.reportService.FileService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

public class MockReportCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);


    public File createTestFileReport(String name, String path) {
        File testFile = new File(path, name);
        if (!testFile.exists()) {
            try {
                Files.createDirectories(Paths.get(path));
                testFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(ANSI_RED + "Can't create file " + testFile + " because of some error-> " + e.getMessage() + ANSI_RESET);
            }
        } else {
            LOGGER.warn(ANSI_PURPLE + "File " + testFile.getName() + " exist in path " + testFile.getPath() + ANSI_RESET);
        }
        LOGGER.info(ANSI_WHITE + " File " + testFile.getName() + ANSI_BLUE + " created successful" + ANSI_RESET);
        return testFile;
    }

    public void delTestFileReport(File file) {
        FileService fileService = new FileService();
        fileService.delFile(file);
    }

    public Map<String, Object[]> citySheetMap() {
        Map<String, Object[]> citySheetMap = new HashMap<>();
        citySheetMap.put("0", new Object[]{"ID", "City name", "Air measurement", "Synoptic measurement"});
        citySheetMap.put("1", new Object[]{"1.0", "Warszawa", "true", "true"});
        citySheetMap.put("2", new Object[]{"2.0", "Poznan", "true", "true"});
        citySheetMap.put("3", new Object[]{"3.0", "Krakow", "true", "false"});
        return citySheetMap;
    }

    public Map<String, Object[]> stationSheetMap() {
        Map<String, Object[]> statonSheetMap = new HashMap<>();
        statonSheetMap.put("0", new Object[]{"ID", "StationID", "Save date", "Name","Street", "City", "Latitude", "Longitude"});
        statonSheetMap.put("1", new Object[]{"1.0", "1.0", LocalDate.now().toString(), "Wawrszawa-Centrum", "Piekna", "Warszawa", "15", "15"});
        statonSheetMap.put("2", new Object[]{"2.0", "2.0", LocalDate.now().toString(), "Poznan-Stoleczna","Stoleczna", "Poznan", "11", "123"});
        statonSheetMap.put("3", new Object[]{"3.0", "3.0", LocalDate.now().toString(), "Krakow-Starowka", "Starowka", "Krakow", "23", "221"});
        return statonSheetMap;
    }

    public Map<String, Object[]> airSheetMap() {
        Map<String, Object[]> airSheetMap = new HashMap<>();
        airSheetMap.put("0", new Object[]{"ID", "StationID", "City", "Measurement date","Save date", "Air quality", "stIndexLevel", "so2IndexLevel","no2IndexLevel", "coIndexLevel", "pm10IndexLevel", "pm25IndexLevel", "o3IndexLevel", "c6h6IndexLevel"});
        airSheetMap.put("1", new Object[]{"1.0", "1.0", "Warszawa", "2018-05-05T12:01:05","2018-05-05T12:01:05", "VERY_BAD", "Very good", "Good","Moderate", "Sufficient", "Bad", "Very bad", "Very good", "Good"});
        airSheetMap.put("2", new Object[]{"2.0", "2.0", "Poznan", "2018-05-11T10:20:20","2018-05-11T10:20:20", "BAD", "null", "so2LEVEL2","noLEVEL2", "coLEVEL2", "pm10LEVEL2", "pm25LEVEL2", "o3LEVEL2", "c6hLEVEL2"});
        airSheetMap.put("3", new Object[]{"3.0", "3.0", "Krakow", "2018-05-11T10:20:20","2018-05-11T10:20:20", "BAD", "null", "so2LEVEL2","noLEVEL2", "coLEVEL2", "pm10LEVEL2", "pm25LEVEL2", "o3LEVEL2", "c6hLEVEL2"});
        return airSheetMap;
    }

    public Map<String, Object[]> synSheetMap() {
        Map<String, Object[]> synSheetMap = new HashMap<>();
        synSheetMap.put("0", new Object[]{"ID", "ForeignID", "Measurement date", "Save date", "City", "Temperature", "Wind speed", "Air Humidity", "Presure"});
        synSheetMap.put("1", new Object[]{"1.0", "1.0", "2019-02-22T18:00:00", "2018-05-05T12:01:05", "Warszawa", "6", "35", "77", "999"});
        synSheetMap.put("2", new Object[]{"2.0", "2.0", "2019-02-22T18:00:00", "2018-05-05T12:01:05", "Poznan", "12", "35", "66", "999"});
        return synSheetMap;
    }
}
