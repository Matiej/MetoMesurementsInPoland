//package pl.testaarosa.airmeasurements;
//
//import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
//public class AirMeasurementsAppVps extends SpringBootServletInitializer {
//
//        protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
//        return applicationBuilder.sources(AirMeasurementsAppVps.class);
//    }
//
//    @Bean
//    public Executor asyncExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(40);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("DataBase-> ");
//        executor.initialize();
//        return executor;
//    }
//}
