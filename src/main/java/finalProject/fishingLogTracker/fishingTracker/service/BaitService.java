package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaitService {

    private final BaitRepository baitRepository;



}
