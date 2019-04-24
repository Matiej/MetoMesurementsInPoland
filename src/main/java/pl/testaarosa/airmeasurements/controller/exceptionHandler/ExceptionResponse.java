package pl.testaarosa.airmeasurements.controller.exceptionHandler;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private LocalDateTime timeStamp;
    private String message;
    private String details;

    public ExceptionResponse(LocalDateTime timeStamp, String message, String details) {
        this.timeStamp = timeStamp;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }
}
