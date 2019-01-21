package pl.testaarosa.airmeasurements.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class WelcomeController {

    @RequestMapping("/")
    public String redirectToUi() {
        return "redirect:/swagger-ui.html#/";
    }
}
