package finalProject.fishingLogTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCaching
public class FishingLogTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FishingLogTrackerApplication.class, args);
	}

}
