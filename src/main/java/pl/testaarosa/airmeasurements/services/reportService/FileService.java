package pl.testaarosa.airmeasurements.services.reportService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Component
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);

    public File createFile(String name, String path) {
        File meteoRaportFile = new File(path, name);
        if (!meteoRaportFile.exists()) {
            try {
                Files.createDirectories(Paths.get(path));
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
}
