package pl.testaarosa.airmeasurements.scheduler;

import org.hibernate.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.testaarosa.airmeasurements.services.AddMeasurementsService;
import pl.testaarosa.airmeasurements.services.EmailNotifierService;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static pl.testaarosa.airmeasurements.services.ConsolerData.*;

@Component
public class AddMesurementsScheduler {


    public static final Logger LOGGER = LoggerFactory.getLogger(AddMesurementsScheduler.class);
    private boolean whiteFlag = true;
    LocalDateTime timeStamp;

    @Autowired
    private AddMeasurementsService addMeasurementsService;
    @Autowired
    private EmailNotifierService emailNotifierService;


//    @Scheduled()
    public void addAllMeasurementsSchedule() {
        LOGGER.info(ANSI_PURPLE + "Add measurements is starting and send e-mail notify" + ANSI_RESET);
        emailNotifierService.sendEmailBeforAddMeasuremetns("first");
        try {
            String report = addMeasurementsService.addMeasurementsAllStations();
            LOGGER.info(ANSI_BLUE + "Measurements added first try successful");
        } catch (Exception e) {
            emailNotifierService.sendFirstErrorMail(e.getMessage());
            e.printStackTrace();
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
                        LOGGER.info(ANSI_BLUE + "Added measurements second try successful!");
                        whiteFlag = false;
                    }
                } catch (TransactionException e) {
                    LOGGER.warn(ANSI_RED + "ADDING MEASUREMENTS SECOND TRY ERROR THROW EXCEPTION. NEXT ATTEMPT TO DOWNLOAD" +
                            " MEASUREMENTS IN 8 HOURS" + ANSI_RESET);
                    whiteFlag=false;
                    emailNotifierService.sendSecondErrorMail(e.getMessage());
                }
            }
        };
        Thread t2 = new Thread(secendTry);
        t2.start();
    }

//    @Scheduled(cron = "0 0/5 * * * *")
//    public void testor() {
//        Runnable secendTry = () -> {
//            int count = 0;
//            while (whiteFlag) {
//                if (LocalDateTime.now().isAfter(timeStamp.plusSeconds(30))) {
//                    LOGGER.info(ANSI_BLUE + "INSIDE WHILE LOOP COUNTER-> " + count + ANSI_RESET);
//                    count++;
//                    sleep(1500);
//                    if (count > 15) {
//                        LOGGER.info(ANSI_YELLOW + "JOB FINISHED. COUNTER IS: " + count + ANSI_RESET);
//                        whiteFlag = false;
//                    } else if (count == new Random().nextInt(15)) {
//                        LOGGER.error(ANSI_RED + "ERROR THROWN FROM THREAD LOOP COUNTER -> " + count + ANSI_RESET);
//                        throw new TransactionException("ERROR THROWN FROM THREAD LOOP");
//                    }
//
//                }
//            }
//        };
//        Thread t2 = new Thread(secendTry);
//
//        LOGGER.info(ANSI_PURPLE + "Add measurements is starting and send email notify" + ANSI_RESET);
//        emailNotifierService.sendEmailBeforAddMeasuremetns();
//        for (int i = 0; i < 11; i++) {
//            LOGGER.info(ANSI_CYAN + "Loop number: -> " + i + ANSI_RESET);
//            sleep(500);
//            if (i == 10) {
//                LOGGER.warn(ANSI_RED + "CIRICAL LOOP SO THROW EXCEPTION AND START THREAD, SAVE DATE SEND MAIL ERROR" + ANSI_RESET);
//                whiteFlag = true;
//                timeStamp = LocalDateTime.now();
//                t2.start();
//            }
//        }
//    }
//
//    private int radomStationId() {
//        int randomId[] = new int[]{117, 129, 114, 52, 11, 920, 530, 870, 10554};
//        return randomId[new Random().nextInt(randomId.length)];
//    }
//
//    private void sleep(int ms) {
//        try {
//            Thread.sleep(ms);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}
