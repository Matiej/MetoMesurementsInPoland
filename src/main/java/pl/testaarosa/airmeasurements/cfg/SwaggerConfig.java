package pl.testaarosa.airmeasurements.cfg;

import com.google.common.base.Predicates;
import com.google.common.collect.Ordering;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        //TODO nie działa sortowanie w swagger -> znalezc
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("pl.testaarosa.airmeasurements.controller"))
                .paths(Predicates.not(PathSelectors.regex("/error")))
                .build()
                .apiInfo(metaInfo())
                .apiDescriptionOrdering(new Ordering<ApiDescription>() {
                    @Override
                    public int compare(ApiDescription tl, ApiDescription tr) {
                        int left = tl.getOperations().size() == 1 ? tl.getOperations().get(0).getPosition() : 0;
                        int right = tr.getOperations().size() == 1 ? tr.getOperations().get(0).getPosition() : 0;
                       int pos = Integer.compare(right,left);
                       if(pos == 0) {
                           pos = tl.getPath().compareTo(tr.getPath())*-1;
                       }
                        return pos;
                    }
                });

    }

    private ApiInfo metaInfo() {
        return new ApiInfoBuilder().title("Meteo")
                .description("Air and weather measurements in Poland")
                .version("0.1")
                .contact(new Contact("Matiej", "www.testaarosa.pl", "maciek@testaarosa.pl"))
                .license("Apache License 2.0")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}