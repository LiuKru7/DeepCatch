package finalProject.fishingLogTracker.fishingTracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CatchNotFoundException.class)
    public ResponseEntity<String> handleCatchNotFound(CatchNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(WeatherNotFoundException.class)
    public ResponseEntity<String> handleWeatherFetch(WeatherNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AquaticNotFoundException.class)
    public ResponseEntity<String> handleAquaticNotFound(AquaticNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(BaitNotFoundException.class)
    public ResponseEntity<String> handleBaitNotFound(BaitNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
