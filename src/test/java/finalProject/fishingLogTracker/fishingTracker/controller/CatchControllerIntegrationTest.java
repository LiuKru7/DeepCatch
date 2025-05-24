package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.auth.service.JwtService;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.*;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.repository.*;
import finalProject.fishingLogTracker.fishingTracker.service.CatchService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static finalProject.fishingLogTracker.auth.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CatchService catchService;

    @Autowired
    private CatchRepository catchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpeciesRepository speciesRepository;

    @Autowired
    private BaitRepository baitRepository;

    @Autowired
    private AquaticRepository aquaticRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private FriendshipRepository friendshipRepository;

    private User testUser;
    private Species testSpecies;
    private Bait testBait;
    private Aquatic testAquatic;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Clean up existing data in correct order
        catchRepository.deleteAll();
        friendshipRepository.deleteAll(); // Delete friendships before users
        speciesRepository.deleteAll();
        baitRepository.deleteAll();
        aquaticRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setRole(ROLE_USER);
        testUser = userRepository.save(testUser);

        // Generate JWT token for the test user
        jwtToken = jwtService.generateToken(testUser);

        // Create test species
        testSpecies = new Species("Trout", "Salmo trutta");
        testSpecies = speciesRepository.save(testSpecies);

        // Create test bait
        testBait = new Bait();
        testBait.setBaitType(BaitType.MINNOW);
        testBait.setDescription("Worm");
        testBait = baitRepository.save(testBait);

        // Create test aquatic
        testAquatic = new Aquatic("Test Lake", AquaticType.LAKE);
        testAquatic = aquaticRepository.save(testAquatic);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test in correct order
        catchRepository.deleteAll();
        friendshipRepository.deleteAll(); // Delete friendships before users
        speciesRepository.deleteAll();
        baitRepository.deleteAll();
        aquaticRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void addCatchWithPhoto_shouldReturnCreatedCatch() throws Exception {
        // Given
        CatchRequest catchRequest = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "Nice catch, big fight!",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        // Mock the JSON part
        MockMultipartFile jsonPart = new MockMultipartFile(
                "catch",
                "catch.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(catchRequest));

        // Mock the file part
        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "photo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes());

        // When
        MvcResult result = mockMvc.perform(multipart("/api/catch")
                        .file(jsonPart)
                        .file(filePart)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        CatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse.class);

        assertThat(response.speciesName()).isEqualTo("Trout");
        assertThat(response.size()).isEqualTo(45.0);
        assertThat(response.weight()).isEqualTo(3.2);
        assertThat(response.fishingStyle()).isEqualTo(FishingStyle.SPINNING);
        assertThat(response.userId()).isEqualTo(testUser.getId());

        // Verify database state
        assertThat(catchRepository.findAll()).hasSize(1);
        assertThat(catchRepository.findAll().get(0).getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void getCatchById_shouldReturnCatch() throws Exception {
        // Given
        CatchRequest catchRequest = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "Nice catch, big fight!",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        CatchResponse createdCatch = catchService.addNewCatch(
                catchRequest,
                new MockMultipartFile("file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());

        // When
        MvcResult result = mockMvc.perform(get("/api/catch/{id}", createdCatch.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse.class);

        assertThat(response.id()).isEqualTo(createdCatch.id());
        assertThat(response.speciesName()).isEqualTo("Trout");
        assertThat(response.size()).isEqualTo(45.0);
    }

    @Test
    void getAllCatches_shouldReturnListOfCatches() throws Exception {
        // Given
        CatchRequest catchRequest1 = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "First catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        CatchRequest catchRequest2 = new CatchRequest(
                testSpecies.getId(),
                50.0,
                4.0,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 20, 10, 30),
                testBait.getId(),
                null,
                false,
                "Second catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        catchService.addNewCatch(
                catchRequest1,
                new MockMultipartFile("file", "photo1.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());
        catchService.addNewCatch(
                catchRequest2,
                new MockMultipartFile("file", "photo2.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());

        // When
        MvcResult result = mockMvc.perform(get("/api/catch")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CatchResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse[].class);

        assertThat(responses).hasSize(2);
        assertThat(responses[0].size()).isEqualTo(45.0);
        assertThat(responses[1].size()).isEqualTo(50.0);
    }

    @Test
    void updateCatch_shouldReturnUpdatedCatch() throws Exception {
        // Given
        CatchRequest initialRequest = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "Initial catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        CatchResponse createdCatch = catchService.addNewCatch(
                initialRequest,
                new MockMultipartFile("file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());

        CatchRequest updateRequest = new CatchRequest(
                testSpecies.getId(),
                50.0,
                4.0,
                testAquatic.getId(),
                FishingStyle.FLY_FISHING,
                LocalDateTime.of(2024, 5, 20, 14, 15),
                testBait.getId(),
                "http://example.com/photo.jpg",
                false,
                "Updated catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        // When
        MvcResult result = mockMvc.perform(put("/api/catch/{id}", createdCatch.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse.class);

        assertThat(response.id()).isEqualTo(createdCatch.id());
        assertThat(response.size()).isEqualTo(50.0);
        assertThat(response.weight()).isEqualTo(4.0);
        assertThat(response.fishingStyle()).isEqualTo(FishingStyle.FLY_FISHING);
        assertThat(response.description()).isEqualTo("Updated catch");

        // Verify database state
        assertThat(catchRepository.findById(createdCatch.id()))
                .isPresent()
                .hasValueSatisfying(catchEntity -> {
                    assertThat(catchEntity.getSize()).isEqualTo(50.0);
                    assertThat(catchEntity.getWeight()).isEqualTo(4.0);
                });
    }

    @Test
    void deleteCatch_shouldDeleteCatchById() throws Exception {
        // Given
        CatchRequest catchRequest = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "Nice catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        CatchResponse createdCatch = catchService.addNewCatch(
                catchRequest,
                new MockMultipartFile("file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());

        // When
        mockMvc.perform(delete("/api/catch/{id}", createdCatch.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        assertThat(catchRepository.findById(createdCatch.id())).isEmpty();
    }

    @Test
    void getCatchesByFishingStyle_shouldReturnListOfCatches() throws Exception {
        // Given
        CatchRequest catchRequest1 = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "Spinning catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        CatchRequest catchRequest2 = new CatchRequest(
                testSpecies.getId(),
                50.0,
                4.0,
                testAquatic.getId(),
                FishingStyle.FLY_FISHING,
                LocalDateTime.of(2024, 5, 20, 10, 30),
                testBait.getId(),
                null,
                false,
                "Fly fishing catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        catchService.addNewCatch(
                catchRequest1,
                new MockMultipartFile("file", "photo1.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());
        catchService.addNewCatch(
                catchRequest2,
                new MockMultipartFile("file", "photo2.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());

        // When
        MvcResult result = mockMvc.perform(get("/api/catch/fishing_style/{style}", FishingStyle.SPINNING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CatchResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse[].class);

        assertThat(responses).hasSize(1);
        assertThat(responses[0].fishingStyle()).isEqualTo(FishingStyle.SPINNING);
        assertThat(responses[0].size()).isEqualTo(45.0);
    }

    @Test
    void getUserCatches_shouldReturnUserCatches() throws Exception {
        // Given
        CatchRequest catchRequest = new CatchRequest(
                testSpecies.getId(),
                45.0,
                3.2,
                testAquatic.getId(),
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                testBait.getId(),
                null,
                false,
                "User's catch",
                testUser.getId(),
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius");

        // Create the catch through the service
        CatchResponse createdCatch = catchService.addNewCatch(
                catchRequest,
                new MockMultipartFile("file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE,
                        "dummy".getBytes()),
                testUser.getId());

        // Verify the catch was created
        assertThat(catchRepository.findById(createdCatch.id())).isPresent();
        assertThat(catchRepository.findByUserId(testUser.getId())).hasSize(1);

        // When
        MvcResult result = mockMvc.perform(get("/api/catch/user")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CatchResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse[].class);

        assertThat(responses).hasSize(1);
        assertThat(responses[0].userId()).isEqualTo(testUser.getId());
        assertThat(responses[0].speciesName()).isEqualTo("Trout");
        assertThat(responses[0].size()).isEqualTo(45.0);
        assertThat(responses[0].weight()).isEqualTo(3.2);
    }

    @Test
    void getCatchById_shouldReturnNotFound_whenCatchDoesNotExist() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/catch/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCatch_shouldReturnBadRequest_whenDataIsInvalid() throws Exception {
        // Given
        CatchRequest invalidRequest = new CatchRequest(
                testSpecies.getId(),
                null,
                null,
                null,
                FishingStyle.FLY_FISHING,
                LocalDateTime.of(2024, 5, 20, 14, 15),
                null,
                null,
                false,
                "Invalid catch",
                testUser.getId(),
                null,
                null,
                null,
                null);

        // When/Then
        mockMvc.perform(put("/api/catch/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}