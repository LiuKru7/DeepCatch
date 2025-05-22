package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.*;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.exception.CatchNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.CatchMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatchServiceTest {

    @Mock
    private CatchRepository catchRepository;
    @Mock
    private CatchMapper catchMapper;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private WeatherService weatherService;
    @Mock
    private WeatherRepository weatherRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SpeciesRepository speciesRepository;
    @Mock
    private BaitRepository baitRepository;
    @Mock
    private AquaticRepository aquaticRepository;
    @Mock
    private ImageService imageService;

    @InjectMocks
    private CatchService catchService;

    private Catch catchEntity;
    private CatchResponse catchResponse;
    private CatchRequest catchRequest;
    private User user;
    private Species species;
    private Bait bait;
    private Aquatic aquatic;
    private Location location;
    private Weather weather;
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        // Create basic test data
        user = new User();
        user.setId(1L);

        species = new Species();
        species.setId(1L);
        species.setName("Pike");

        bait = new Bait();
        bait.setId(1L);
        bait.setBaitType(BaitType.SPINNER);

        aquatic = new Aquatic();
        aquatic.setId(1L);
        aquatic.setName("Lake");

        location = new Location();
        location.setId(1L);
        location.setLatitude(55.0);
        location.setLongitude(24.0);

        weather = new Weather();
        weather.setId(1L);

        // Create catch entity
        catchEntity = new Catch();
        catchEntity.setId(1L);
        catchEntity.setUser(user);
        catchEntity.setSpecies(species);
        catchEntity.setBait(bait);
        catchEntity.setAquatic(aquatic);
        catchEntity.setLocation(location);
        catchEntity.setWeather(weather);
        catchEntity.setFishingStyle(FishingStyle.SPINNING);

        // Create catch response
        catchResponse = new CatchResponse(
                1L, // id
                1L, // speciesId
                "Pike", // speciesName
                "Lake", // aquaticName
                "Esox lucius", // speciesLatinName
                null, // aquaticType
                FishingStyle.SPINNING, // fishingStyle
                "photo.jpg", // photoUrl
                50.0, // size
                2.5, // weight
                1L, // aquaticId
                LocalDateTime.now(), // time
                1L, // baitId
                "Spinner bait", // baitDescription
                BaitType.SPINNER, // baitType
                true, // isReleased
                "Caught a nice pike", // description
                1L, // userId
                55.0, // latitude
                24.0, // longitude
                "Lithuania", // country
                "Vilnius" // district
        );

        // Create catch request
        catchRequest = new CatchRequest(
                1L, // speciesId
                50.0, // size
                2.5, // weight
                1L, // aquaticId
                FishingStyle.SPINNING, // fishingStyle
                LocalDateTime.now(), // time
                1L, // baitId
                "photo.jpg", // photoUrl
                true, // isReleased
                "Caught a nice pike", // description
                1L, // userId
                55.0, // latitude
                24.0, // longitude
                "Lithuania", // country
                "Vilnius" // district
        );

        // Create test file
        file = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes());
    }

    @Test
    void getCatchById_ShouldReturnCatchResponse() {
        // Arrange
        when(catchRepository.findById(1L)).thenReturn(Optional.of(catchEntity));
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        CatchResponse result = catchService.getCatchById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(catchResponse, result);
    }

    @Test
    void getCatchById_ShouldThrowException_WhenCatchNotFound() {
        // Arrange
        when(catchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CatchNotFoundException.class, () -> catchService.getCatchById(1L));
    }

    @Test
    void getAllCatches_ShouldReturnListOfCatchResponses() {
        // Arrange
        List<Catch> catches = Arrays.asList(catchEntity);
        when(catchRepository.findAll()).thenReturn(catches);
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        List<CatchResponse> result = catchService.getAllCatches();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(catchResponse, result.get(0));
    }

    @Test
    void updateCatch_ShouldReturnUpdatedCatchResponse() {
        // Arrange
        when(catchRepository.findById(1L)).thenReturn(Optional.of(catchEntity));
        when(catchMapper.toCatch(catchRequest)).thenReturn(catchEntity);
        when(catchRepository.save(any(Catch.class))).thenReturn(catchEntity);
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        CatchResponse result = catchService.updateCatch(1L, catchRequest);

        // Assert
        assertNotNull(result);
        assertEquals(catchResponse, result);
    }

    @Test
    void updateCatch_ShouldThrowException_WhenCatchNotFound() {
        // Arrange
        when(catchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CatchNotFoundException.class, () -> catchService.updateCatch(1L, catchRequest));
    }

    @Test
    void deleteCatch_ShouldDeleteCatch() {
        // Arrange
        when(catchRepository.existsById(1L)).thenReturn(true);

        // Act
        catchService.deleteCatch(1L);

        // Assert
        verify(catchRepository).deleteById(1L);
    }

    @Test
    void deleteCatch_ShouldThrowException_WhenCatchNotFound() {
        // Arrange
        when(catchRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(CatchNotFoundException.class, () -> catchService.deleteCatch(1L));
    }

    @Test
    void getCatchesByFishingStyle_ShouldReturnListOfCatchResponses() {
        // Arrange
        List<Catch> catches = Arrays.asList(catchEntity);
        when(catchRepository.findByFishingStyle(FishingStyle.SPINNING)).thenReturn(catches);
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        List<CatchResponse> result = catchService.getCatchesByFishingStyle(FishingStyle.SPINNING);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(catchResponse, result.get(0));
    }

    @Test
    void getCatchesByBait_ShouldReturnListOfCatchResponses() {
        // Arrange
        List<Catch> catches = Arrays.asList(catchEntity);
        when(catchRepository.findByBait_BaitType(BaitType.SPINNER)).thenReturn(catches);
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        List<CatchResponse> result = catchService.getCatchesByBait(BaitType.SPINNER);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(catchResponse, result.get(0));
    }

    @Test
    void getCatchesByUser_ShouldReturnListOfCatchResponses() {
        // Arrange
        List<Catch> catches = Arrays.asList(catchEntity);
        when(catchRepository.findByUserId(1L)).thenReturn(catches);
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        List<CatchResponse> result = catchService.getCatchesByUser(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(catchResponse, result.get(0));
    }

    @Test
    void addNewCatch_ShouldReturnCatchResponse() {
        // Arrange
        when(catchMapper.toCatch(catchRequest)).thenReturn(catchEntity);
        when(aquaticRepository.findById(1L)).thenReturn(Optional.of(aquatic));
        when(baitRepository.findById(1L)).thenReturn(Optional.of(bait));
        when(speciesRepository.findById(1L)).thenReturn(Optional.of(species));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(weatherService.fetchWeather(anyDouble(), anyDouble())).thenReturn(weather);
        when(weatherRepository.save(any(Weather.class))).thenReturn(weather);
        when(imageService.saveFile(any(MultipartFile.class))).thenReturn("photo.jpg");
        when(catchRepository.save(any(Catch.class))).thenReturn(catchEntity);
        when(catchMapper.toCatchResponse(catchEntity)).thenReturn(catchResponse);

        // Act
        CatchResponse result = catchService.addNewCatch(catchRequest, file, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(catchResponse, result);
    }

    @Test
    void addNewCatch_ShouldThrowException_WhenAquaticNotFound() {
        // Arrange
        when(catchMapper.toCatch(catchRequest)).thenReturn(catchEntity);
        when(aquaticRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> catchService.addNewCatch(catchRequest, file, 1L));
    }
}