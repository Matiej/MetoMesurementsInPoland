package pl.testaarosa.airmeasurements.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class WelcomeController {

    @GetMapping("/")
    public String redirectToUi() {
        return "redirect:/swagger-ui.html#/";
    }

    @GetMapping("/hal")
    public String redirectToHal() {
        return "redirect:/browser/index.html#/";
    }
}
