package pl.testaarosa.airmeasurements.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class Converter {

    public String jsonInString(List<?> objects) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(objects);
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }


    public String jsonInString(Object object) {

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";

        try {
            jsonInString = mapper.writeValueAsString(object);
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }
}
