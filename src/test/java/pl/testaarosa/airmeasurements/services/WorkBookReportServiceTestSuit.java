package pl.testaarosa.airmeasurements.services;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.testaarosa.airmeasurements.domain.AirMeasurement;
import pl.testaarosa.airmeasurements.domain.MeasuringStation;
import pl.testaarosa.airmeasurements.domain.SynopticMeasurement;
import pl.testaarosa.airmeasurements.repositories.MockMeasuringStationRepository;
import pl.testaarosa.airmeasurements.repositories.MockReportCreator;
import pl.testaarosa.airmeasurements.repositories.MockSynopticMeasurementRepository;
import pl.testaarosa.airmeasurements.services.reportService.FileService;
import pl.testaarosa.airmeasurements.services.reportService.SheetStyles;
import pl.testaarosa.airmeasurements.services.reportService.WorkBookReportServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class WorkBookReportServiceTestSuit {

    private WorkBookReportServiceImpl workBookReportService;
    private FileService fileService;
    private SheetStyles stylesheet;
    private MockMeasuringStationRepository mockMeasuringStationRepository;
    private MockSynopticMeasurementRepository mockSynopticMeasurementRepository;
    private MockReportCreator mockReportCreator;

    @Before
    public void init() {
        mockMeasuringStationRepository = new MockMeasuringStationRepository();
        mockSynopticMeasurementRepository = new MockSynopticMeasurementRepository();
        mockReportCreator = new MockReportCreator();
        stylesheet = new SheetStyles();
        fileService = new FileService();
    }

    @Test
    public void shouldCreateXMLAddAllMeasurementsReport() throws Exception {
        //given
        LinkedHashMap<MeasuringStation, AirMeasurement> mStAirMeasurementsMap = mockMeasuringStationRepository.measurementMap();
        LinkedHashMap<String, SynopticMeasurement> synopticMeasurementsMap = mockSynopticMeasurementRepository.measurementMap();
        SynopticMeasurement synopticMeasurement = synopticMeasurementsMap.get("Krakow");
        synopticMeasurementsMap.remove("Krakow", synopticMeasurement);
        //when
        workBookReportService = new WorkBookReportServiceImpl(stylesheet, fileService);
        File xmlAddAllMeasurementsReport = workBookReportService.createXMLAddAllMeasurementsReport(synopticMeasurementsMap, mStAirMeasurementsMap);
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(xmlAddAllMeasurementsReport));
        HSSFSheet citySheet = workbook.getSheetAt(0);
        HSSFSheet stationSheet = workbook.getSheetAt(1);
        HSSFSheet airSheet = workbook.getSheetAt(2);
        HSSFSheet synSheet = workbook.getSheetAt(3);
        //then
        sheetAssertion(mockReportCreator.citySheetMap(), citySheet);
        sheetAssertion(mockReportCreator.stationSheetMap(), stationSheet);
        sheetAssertion(mockReportCreator.airSheetMap(), airSheet);
        sheetAssertion(mockReportCreator.synSheetMap(), synSheet);
    }

    private void sheetAssertion(Map<String, Object[]> map, HSSFSheet citySheet) {
        assertEquals(map.size(), citySheet.getPhysicalNumberOfRows());
        Iterator<Row> citySheetIterator = citySheet.iterator();
        while (citySheetIterator.hasNext()) {
            Row currentRow = citySheetIterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();
            int rowNum = currentRow.getRowNum();
            Object[] cellsObj = map.get(String.valueOf(rowNum));
            while (cellIterator.hasNext()) {
                Cell currentCell = cellIterator.next();
                int columnIndex = currentCell.getColumnIndex();
                if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    assertEquals(Double.parseDouble((String) cellsObj[columnIndex]), currentCell.getNumericCellValue());
                }
                if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                    assertEquals(cellsObj[columnIndex], currentCell.getStringCellValue());
                }
            }
        }
    }
}
