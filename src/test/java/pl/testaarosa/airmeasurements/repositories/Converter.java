package pl.testaarosa.airmeasurements.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class Converter<T> {

    public String jsonInString(T object) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        try {
            jsonInString = mapper.writeValueAsString(object);
//            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }
}
