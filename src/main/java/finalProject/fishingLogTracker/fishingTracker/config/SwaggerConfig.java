package finalProject.fishingLogTracker.fishingTracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fishing Log Tracker API")
                        .description("API documentation for Fishing Log Tracker application")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Fishing Log Tracker Team")
                                .email("your-email@example.com")));
    }
} 