package finalProject.fishingLogTracker.fishingTracker.controller;

import finalProject.fishingLogTracker.fishingTracker.service.BaitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/bait")
@RequiredArgsConstructor
public class BaitController {

    private final BaitService baitService;



}
