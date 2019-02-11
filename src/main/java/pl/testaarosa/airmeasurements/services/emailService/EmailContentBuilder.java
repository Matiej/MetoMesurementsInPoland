package pl.testaarosa.airmeasurements.services.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailContentBuilder {

    private final TemplateEngine templateEngine;

    @Autowired
    public EmailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String multiVarBuilder(Map<String, Object> messages) {
        Context context = new Context();
        context.setVariables(messages);
        return templateEngine.process("mailTemplate", context);
    }
}
