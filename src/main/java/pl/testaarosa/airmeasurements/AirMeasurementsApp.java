package pl.testaarosa.airmeasurements;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
public class AirMeasurementsApp {

    public static void main(String[] args) {
        SpringApplication.run(AirMeasurementsApp.class, args);
    }
}
