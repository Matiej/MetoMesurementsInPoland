package pl.testaarosa.airmeasurements.services;

import pl.testaarosa.airmeasurements.services.reportService.FileService;

import java.io.File;
import java.time.LocalDateTime;

public class MockReportCreator {
    private static final String PATH = new File(System.getProperty("user.dir")
            + File.separator + "src/test/java/pl/testaarosa/airmeasurements/repositories/mockReport").getAbsolutePath();
    private static final String NAME_CONST = "_addAllMeasurementsReportTEST.xls";

    public File createTestFileReport() {
        FileService fileService = new FileService();
        String reportFileName = LocalDateTime.now().withNano(0).toString().replaceAll(":", "-") + NAME_CONST;
        return fileService.createFile(reportFileName, PATH);
    }

    public void delTestFileReport(File file) {
        FileService fileService = new FileService();
        fileService.delFile(file);
    }
}
