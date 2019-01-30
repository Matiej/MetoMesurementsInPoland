package pl.testaarosa.airmeasurements.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Component
public class AddMesurementsSchedulerService {


    public static final Logger LOGGER = LoggerFactory.getLogger(AddMesurementsSchedulerService.class);
    private boolean whiteFlag = true;
    LocalDateTime timeStamp;

    @Autowired
    private AddMeasurementsService addMeasurementsService;
    @Autowired
    private EmailNotifierService emailNotifierService;


    @Scheduled(cron = "0 01 13,21,5 * * *")
//    @Scheduled(cron = "0 0/15 * 1/1 * *")
    public void addAllMeasurementsSchedule() {
        LOGGER.info(ANSI_PURPLE + "Add measurements is starting and send e-mail notify" + ANSI_RESET);
        emailNotifierService.sendEmailBeforAddMeasuremetns("first");
        try {
            addMeasurementsService.addMeasurementsAllStations();
            LOGGER.info(ANSI_BLUE + "Measurements added first try successful"+ ANSI_RESET);
        } catch (Exception e) {
            e.printStackTrace();
            emailNotifierService.sendFirstErrorMail(e.toString());
            LOGGER.warn(ANSI_RED + "ADDING MEASUREMENTS ERROR, THROW EXCEPTION AND START SECOND TRY THREAD," +
                    " SAVE DATE AND SEND E-MAIL" + ANSI_RESET);
            whiteFlag = true;
            timeStamp = LocalDateTime.now();
            secondAddTry();
        }
    }

    private void secondAddTry() {
        Runnable secendTry = () -> {
            while (whiteFlag) {
                try {
                    if (LocalDateTime.now().isAfter(timeStamp.plusMinutes(10))) {
                        LOGGER.info(ANSI_PURPLE + "Trying add all measurements second time. " + ANSI_RESET);
                        emailNotifierService.sendEmailBeforAddMeasuremetns("second");
                        addMeasurementsService.addMeasurementsAllStations();
                        LOGGER.info(ANSI_BLUE + "Added measurements second try successful!");
                        whiteFlag = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.warn(ANSI_RED + "ADDING MEASUREMENTS SECOND TRY ERROR THROW EXCEPTION. NEXT ATTEMPT TO DOWNLOAD" +
                            " MEASUREMENTS IN 8 HOURS" + ANSI_RESET);
                    whiteFlag=false;
                    emailNotifierService.sendSecondErrorMail(e.toString());
                }
            }
        };
        Thread t2 = new Thread(secendTry);
        t2.start();
    }
}
