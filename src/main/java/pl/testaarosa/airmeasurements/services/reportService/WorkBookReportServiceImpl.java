package pl.testaarosa.airmeasurements.services.reportService;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationBeanFactoryMetadata;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.City;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WorkBookReportServiceImpl implements WorkBookReportService {

    private static final String PATH = new File(System.getProperty("user.dir") + File.separator + "reports").getAbsolutePath();
    private static final String PATH_UBUNTU_TOMCAT = new File(System.getProperty("user.home") + File.separator + "reports").getAbsolutePath();
    private static final String NAME_CONST = "_addAllMeasurementsReport.xls";
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkBookReportServiceImpl.class);
    private HSSFWorkbook workbook;
    private Row row;
    private LinkedHashMap<String, Object[]> cityMap;
    private LinkedHashMap<String, Object[]> stationMap;
    private LinkedHashMap<String, Object[]> airMstMap;
    private LinkedHashMap<String, Object[]> synoptcMstMap;

    private final SheetStyles sheetStyles;
    private final FileService fileService;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    public WorkBookReportServiceImpl(SheetStyles sheetStyles, FileService fileService) {
        this.sheetStyles = sheetStyles;
        this.fileService = fileService;
    }

    @Override
    public  File createXMLAddAllMeasurementsReport(LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap,
                                                  LinkedHashMap<MeasuringStation, AirMeasurement> mStResponseMap) throws Exception {
            String reportFileName = LocalDateTime.now().withNano(0).toString().replaceAll(":", "-") + NAME_CONST;
            File file = fileService.createFile(reportFileName, PATH);

            try (OutputStream out = new FileOutputStream(file)) {
                workbook = new HSSFWorkbook();
                HSSFCellStyle stationStyle = sheetStyles.cellStyle(workbook, "station");
                HSSFCellStyle ordinaryStyle = sheetStyles.cellStyle(workbook, "ordinary");
                HSSFCellStyle synopticStyle = sheetStyles.cellStyle(workbook, "synoptic");
                HSSFCellStyle airStyle = sheetStyles.cellStyle(workbook, "air");

                createCity_Station_AirMap(mStResponseMap, synopticMeasurementMap);
                createSynoptic(synopticMeasurementMap);

                HSSFSheet citySheet = workbook.createSheet("City");
                HSSFSheet statioonsSheet = workbook.createSheet("Stations");
                HSSFSheet airMstSheet = workbook.createSheet("Air_Measurements");
                HSSFSheet synMstSheet = workbook.createSheet("Synoptic_Measurement");

                sheetCellFill(stationStyle, ordinaryStyle, citySheet, cityMap);
                sheetCellFill(stationStyle, ordinaryStyle, statioonsSheet, stationMap);
                sheetCellFill(airStyle, ordinaryStyle, airMstSheet, airMstMap);
                sheetCellFill(synopticStyle, ordinaryStyle, synMstSheet, synoptcMstMap);

                workbook.write(out);
                LOGGER.info(ANSI_BLUE + "Report saved successful in => " + ANSI_PURPLE + file + ANSI_RESET);
            } catch (IOException e) {
                LOGGER.error("Can't generate XML report! " + e);
                throw new Exception(e);
        }
        return file;
    }

    @Override
    public synchronized String delOldReports() {
        File[] listFiles = new File(PATH).listFiles();
        AtomicInteger delFileCounter = new AtomicInteger(0);
        int noReportsInDb = Objects.requireNonNull(listFiles).length;
        if (noReportsInDb > 30) {
            Stream.of(listFiles).forEach(r -> {
                LocalDateTime lastFileModifiedDate = LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(r.lastModified()), ZoneId.systemDefault());
                if (lastFileModifiedDate.isBefore(LocalDateTime.now().minusDays(1))) {
                    fileService.delFile(r);
                    delFileCounter.getAndIncrement();
                }
            });
            File[] filesInDb = new File(PATH).listFiles();
            int noOfFilesInDb = Objects.requireNonNull(filesInDb).length;
            LOGGER.warn("Deleted " + delFileCounter.get() + " reports files. Number of reports left in data base: " + noOfFilesInDb);
            return "Deleted " + delFileCounter.get() + " reports files. Number of reports left in data base: " + noOfFilesInDb;
        } else {
            LOGGER.info("No reports deleted because number of reports in data base is less than 30. Reports in database: " + noReportsInDb);
            return "No reports deleted because number of reports in data base is less than 30. Reports in database: " + noReportsInDb;
        }
    }

    private void createCity_Station_AirMap(LinkedHashMap<MeasuringStation, AirMeasurement> mStResponseMap,
                                           LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap) {
        cityMap = new LinkedHashMap<>();
        cityMap.put("0", new Object[]{"ID", "City name", "Air measurement", "Synoptic measurement"});

        stationMap = new LinkedHashMap<>();
        stationMap.put("0", new String[]{"ID", "StationID", "Save date", "Name", "Street", "City", "Latitude", "Longitude"});

        airMstMap = new LinkedHashMap<>();
        airMstMap.put("0", new String[]{"ID", "StationID", "City", "Measurement date", "Save date", "Air quality",
                "stIndexLevel", "so2IndexLevel", "no2IndexLevel", "coIndexLevel", "pm10IndexLevel", "pm25IndexLevel",
                "o3IndexLevel", "c6h6IndexLevel"});

        AtomicInteger row = new AtomicInteger(1);
        AtomicInteger cityRow = new AtomicInteger(1);
        mStResponseMap.entrySet().forEach(c -> {
            AirMeasurement airMst = c.getValue();
            City city = airMst.getCity();
            MeasuringStation station = c.getKey();
            if (cityMap.values().stream().noneMatch(v -> v[1].equals(city.getCityName()))) {
                cityMap.put(String.valueOf(cityRow.get()), new Object[]{cityRow.get(), city.getCityName(),
                        true,
                        synopticMeasurementMap.keySet().stream().anyMatch(t -> t.equalsIgnoreCase(city.getCityName()))});
                cityRow.getAndIncrement();
            }
            stationMap.put(String.valueOf(row.get()), new Object[]{row.get(), station.getStationId(),
                    LocalDate.now(), station.getStationName(), station.getStreet(), station.getCity(), station.getLatitude(),
                    station.getLongitude()});
            airMstMap.put(String.valueOf(row.get()), new Object[]{row.get(), station.getStationId(), station.getCity(),
                    airMst.getMeasurementDate(), airMst.getSaveDate(), airMst.getAirQuality().toString(),
                    airMst.getStIndexLevel(), airMst.getSo2IndexLevel(), airMst.getNo2IndexLevel(), airMst.getCoIndexLevel(),
                    airMst.getPm10IndexLevel(), airMst.getPm25IndexLevel(), airMst.getO3IndexLevel(), airMst.getC6h6IndexLevel()});
            row.getAndIncrement();
        });
    }

    private void createSynoptic(LinkedHashMap<String, SynopticMeasurement> synopticMeasurementMap) {
        Iterator<Map.Entry<String, Object[]>> iterator = cityMap.entrySet().iterator();
        String last = "";
        while (iterator.hasNext()) {
            last = iterator.next().getKey();
        }
        AtomicInteger cityRow = new AtomicInteger(Integer.valueOf(last) + 1);
        synoptcMstMap = new LinkedHashMap<>();
        synoptcMstMap.put("0", new Object[]{"ID", "ForeignID", "Measurement date", "Save date", "City",
                "Temperature", "Wind speed", "Air Humidity", "Presure"});
        AtomicInteger row = new AtomicInteger(1);
        synopticMeasurementMap.forEach((key, value) -> {
            SynopticMeasurement synMst = value;
//            String msDate = synMst.getMeasurementDate() + "T" + synMst.getMeasurementHour() + ":00:00";
            synoptcMstMap.put(String.valueOf(row.get()), new Object[]{row.get(), synMst.getForeignId(), synMst.getMeasurementDate(),
                    synMst.getSaveDate(), synMst.getCityName(), synMst.getTemperature(), synMst.getWindSpeed(), synMst.getAirHumidity(),
                    synMst.getPressure()});
            row.getAndIncrement();
            if (cityMap.values().stream().noneMatch(c -> c[1].equals(key))) {
                City city = synMst.getCity();
                cityMap.put(String.valueOf(cityRow.get()), new Object[]{cityRow.get(), city.getCityName(),
                        false, true});
                cityRow.getAndIncrement();
            }
        });
    }

    private void sheetCellFill(HSSFCellStyle headerStyle, HSSFCellStyle rowsStyle, HSSFSheet sheet,
                               Map<String, Object[]> cellsMap) {

        cellsMap.forEach((key, cells) -> {
            int rowId = Integer.parseInt(key);
            row = sheet.createRow(rowId);
            IntStream.range(0, cells.length).forEach(t -> {
                Cell cell = row.createCell(t);
                sheet.autoSizeColumn(t);
                if (cell.getRowIndex() == 0) {
                    cell.setCellStyle(headerStyle);
//                    sheet.setColumnWidth(t, 7000);
                } else {
                    cell.setCellStyle(rowsStyle);
                }
                if (rowId == 0) {
                    cell.setCellValue(cells[t].toString());
                } else {
                    if (cells[t] instanceof Double) {
                        cell.setCellValue((Double) cells[t]);
                    } else if (cells[t] instanceof Integer) {
                        cell.setCellValue((Integer) cells[t]);
                    } else if (cells[t] instanceof Long) {
                        cell.setCellValue((Long) cells[t]);
                    } else if (cells[t] == null) {
                        cell.setCellValue("null");
                    } else {
                        if (cells[t].toString().equals("false")) {
                            cell.setCellStyle(sheetStyles.cellStyle(workbook, "false"));
                            cell.setCellValue(cells[t].toString());
                        } else {
                            cell.setCellValue(cells[t].toString());
                        }
                    }
                    cell.removeCellComment();
                }
            });
        });
    }
}
