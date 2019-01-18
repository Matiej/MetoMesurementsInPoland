package pl.testaarosa.airmeasurements.services;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Service
public class AddMeasurementRaportGenerator {
    //TODO create file jako metoda prv, sheetstyles jako metoda prv.
// Wymieszać wszystkie style w mteodzie. Ma przyjmować stringa i na tej podstawie definiować ktory styl zwracac=
    //zrobić beany. moze sie przyda
    private static final String PATH = new File(System.getProperty("user.dir") + "/reports").getAbsolutePath();
    private static final String NAME_COST = "_allMeasurementsReport.xls";
    private static final Logger LOGGER = LoggerFactory.getLogger(AddMeasurementRaportGenerator.class);
    private HSSFWorkbook workbook;
    private Row row;
    private HSSFCellStyle airHeaderStyle;
    private HSSFCellStyle style;
    private HSSFFont font;
    private File meteoRaportFile;

    public String createXMLReport(List<MeasuringStation> mSt) {
        File file = createFile(LocalDateTime.now().withNano(0).toString().replaceAll(":","-") + NAME_COST);
        try (OutputStream out = new FileOutputStream(file)) {
            workbook = new HSSFWorkbook();
            HSSFCellStyle stationStyle = cellStyle(workbook, "station");
            HSSFCellStyle ordinaryStyle = cellStyle(workbook, "ordinary");

            HSSFSheet statioonsSheet = workbook.createSheet("Stations");
            Map<String, Object[]> stationMap = new HashMap<>();
            stationMap.put("0", new String[]{"ID", "ForeignID", "Save date", "Name", "Street", "City", "Latitude", "Longitude"});
            IntStream.range(0, mSt.size())
                    .forEach(i -> {
                        Long id = mSt.get(i).getId();
                        Integer stId = mSt.get(i).getStationId();
                        LocalDate svDate = LocalDate.now();
                        String stName = mSt.get(i).getStationName();
                        String city = mSt.get(i).getCity();
                        String street = mSt.get(i).getStreet();
                        String latitude = mSt.get(i).getLatitude();
                        String longitude = mSt.get(i).getLongitude();
                        Object[] row = new Object[]{id, stId, svDate, stName, street, city, latitude, longitude};
                        stationMap.put(String.valueOf(i + 1), row);
                    });
            sheetCellFill(stationStyle,ordinaryStyle,statioonsSheet,stationMap);
            workbook.write(out);
            LOGGER.info(ANSI_BLUE+ "Report saved successful in => " + ANSI_PURPLE +file + ANSI_RESET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO zwrocić w zależnosci co będzie potrzebował mail. Może ścieżkę lub cały obiekt. A
        // TODO go nie zapisywac do pliku tylko prosto do załącznika.
        return "";
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
                CellStyle cellStyle = cell.getCellStyle();
                cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                if (rowId == 0) {
                    cell.setCellValue(cells[t].toString());
                } else {
                    if (cells[t] instanceof Double) {
                        cell.setCellValue((Double) cells[t]);
                    } else if (cells[t] instanceof Integer) {
                        cell.setCellValue((Integer) cells[t]);
                    } else if (cells[t] instanceof Long){
                        cell.setCellValue((Long) cells[t]);
                    } else {
                        cell.setCellValue(cells[t].toString());
                    }
                    cell.removeCellComment();
                }
            });
        });
    }

    private File createFile(String name) {
        meteoRaportFile = new File(PATH, name);
        if (!meteoRaportFile.exists()) {
            try {
                Files.createDirectories(Paths.get(PATH));
                meteoRaportFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(ANSI_RED + "Can't create file " + meteoRaportFile + " because of some error-> " + e.getMessage() + ANSI_RESET);
            }
        } else {
            LOGGER.warn(ANSI_PURPLE + "File " + meteoRaportFile.getName() + " exist in path " + meteoRaportFile.getPath() + ANSI_RESET);
        }
        LOGGER.info(ANSI_WHITE + " File " + meteoRaportFile.getName() + ANSI_BLUE + " created successful" + ANSI_RESET);
        return meteoRaportFile;
    }

    private HSSFCellStyle cellStyle(HSSFWorkbook workbook, String styleName) {
        //air, /ordinary/ syn/ stat
        font = workbook.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        if (styleName.equalsIgnoreCase("ordinary")) {
            font.setFontHeightInPoints((short) 8);
        } else {
            font.setFontHeightInPoints((short) 9);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            switch (styleName.toLowerCase()) {
                case "air":
                    font.setColor(HSSFColor.BLUE.index);
                    break;
                case "synoptic":
                    font.setColor(HSSFColor.ORANGE.index);
                    break;
                case "station":
                    font.setColor(HSSFColor.DARK_YELLOW.index);
                    break;
                default:
                    font.setColor(HSSFColor.BLACK.index);
                    break;
            }
        }
        style = workbook.createCellStyle();
        if (styleName.equalsIgnoreCase("ordinary")) {
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        } else {
            style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
            style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
        }
        style.setFont(font);
        return style;
    }
}
