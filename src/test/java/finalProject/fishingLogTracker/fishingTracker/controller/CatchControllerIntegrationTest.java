package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.auth.model.User;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.CatchResponse;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.exception.CatchNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.service.CatchService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static finalProject.fishingLogTracker.auth.enums.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CatchService catchService;

    private CatchResponse mockedCatch;
    private CatchResponse mockedCatch2;
    private List<CatchResponse> mockedCatchList;
    private List<CatchResponse> spinningCatchList;

    @BeforeEach
    public void setup() {

        mockedCatch = new CatchResponse(
                1L,
                10L,
                "Trout",
                "Lake",
                "Salmo trutta",
                AquaticType.RIVER,
                FishingStyle.SPINNING,
                "photo.jpg",
                45.0,
                3.2,
                2L,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                5L,
                "Worm",
                BaitType.MINNOW,
                false,
                "Nice catch, big fight!",
                42L,
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius"
        );

        mockedCatch2 = new CatchResponse(
                2L,
                10L,
                "Pike",
                "River",
                "Esox lucius",
                AquaticType.RIVER,
                FishingStyle.SPINNING,
                "pike.jpg",
                55.0,
                4.1,
                3L,
                LocalDateTime.of(2024, 5, 20, 12, 45),
                7L,
                "Spinner",
                BaitType.SPINNER,
                false,
                "Great pike catch!",
                43L,
                54.7012,
                25.2889,
                "Lithuania",
                "Vilnius"
        );

        mockedCatchList = List.of(mockedCatch, mockedCatch2);
        spinningCatchList = List.of(mockedCatch, mockedCatch2);
    }

    @Test
    void addCatchWithPhoto_shouldReturnCreatedCatch() throws Exception {

        // Mock authenticated user
        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setUsername("testuser");
        mockUser.setRole(ROLE_USER);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        CatchRequest catchRequest = new CatchRequest(
                1L,
                45.0,
                3.2,
                2L,
                FishingStyle.SPINNING,
                LocalDateTime.of(2024, 5, 19, 10, 30),
                5L,
                null,
                false,
                "Nice catch, big fight!",
                42L,
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius"
        );


        // Mock the JSON part
        MockMultipartFile jsonPart = new MockMultipartFile(
                "catch",
                "catch.json",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(catchRequest)
        );

        // Mock the file part
        MockMultipartFile filePart = new MockMultipartFile(
                "file",
                "photo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy image content".getBytes()
        );

        Mockito.when(catchService.addNewCatch(
                Mockito.any(CatchRequest.class),
                Mockito.any(MultipartFile.class),
                Mockito.eq(mockUser.getId())
        )).thenReturn(mockedCatch);

        MvcResult result = mockMvc.perform(multipart("/api/catch")
                        .file(jsonPart)
                        .file(filePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse.class
        );

        assertThat(response.id()).isEqualTo(mockedCatch.id());
        assertThat(response.speciesName()).isEqualTo("Trout");

        SecurityContextHolder.clearContext();
    }

    @Test
    void getCatchById_shouldReturnCatch() throws Exception {

        Mockito.when(catchService.getCatchById(1L)).thenReturn(mockedCatch);

        MvcResult result = mockMvc.perform(get("/api/catch/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse.class
        );

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.speciesName()).isEqualTo("Trout");
    }

    @Test
    void getAllCatches_shouldReturnListOfCatches() throws Exception {

        Mockito.when(catchService.getAllCatches()).thenReturn(mockedCatchList);

        MvcResult result = mockMvc.perform(get("/api/catch")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CatchResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse[].class
        );

        assertThat(responses).hasSize(2);
        assertThat(responses[0].id()).isEqualTo(1);
    }

    @Test
    void updateCatch_shouldReturnUpdatedCatch() throws Exception {

        CatchRequest updateRequest = new CatchRequest(
                1L,
                50.0,
                3.5,
                2L,
                FishingStyle.FLY_FISHING,
                LocalDateTime.of(2024, 5, 20, 14, 15), // updated date
                6L,
                null,
                false,
                "Updated description - even bigger fight!",
                42L,
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius"
        );

        CatchResponse updatedResponse = new CatchResponse(
                1L,
                10L,
                "Trout",
                "Lake",
                "Salmo trutta",
                AquaticType.RIVER,
                FishingStyle.FLY_FISHING,
                "photo.jpg",
                50.0,
                3.5,
                2L,
                LocalDateTime.of(2024, 5, 20, 14, 15),
                6L,
                "Dry fly",
                BaitType.FLY,
                false,
                "Updated description - even bigger fight!",
                42L,
                54.6872,
                25.2797,
                "Lithuania",
                "Vilnius"
        );

        Mockito.when(catchService.updateCatch(
                Mockito.eq(1L),
                Mockito.any(CatchRequest.class))
        ).thenReturn(updatedResponse);

        MvcResult result = mockMvc.perform(put("/api/catch/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        CatchResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse.class
        );

        assertThat(response.weight()).isEqualTo(3.5);
        assertThat(response.size()).isEqualTo(50.0);
        assertThat(response.fishingStyle()).isEqualTo(FishingStyle.FLY_FISHING);
        assertThat(response.baitType()).isEqualTo(BaitType.FLY);

    }

    @Test
    void deleteCatch_shouldDeleteCatchById() throws Exception {

        Mockito.doNothing().when(catchService).deleteCatch(1L);

        mockMvc.perform(delete("/api/catch/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Mockito.verify(catchService, Mockito.times(1)).deleteCatch(1L);
    }

    @Test
    void getCatchesByFishingStyle_shouldReturnListOfCatches() throws Exception {

        Mockito.when(catchService.getCatchesByFishingStyle(FishingStyle.SPINNING))
                .thenReturn(spinningCatchList);

        MvcResult result = mockMvc.perform(get("/api/catch/fishing_style/{style}", FishingStyle.SPINNING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CatchResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse[].class
        );

        assertThat(responses).hasSize(2);
        assertThat(responses[0].fishingStyle()).isEqualTo(FishingStyle.SPINNING);
        assertThat(responses[1].fishingStyle()).isEqualTo(FishingStyle.SPINNING);
        assertThat(responses[0].speciesName()).isEqualTo("Trout");
        assertThat(responses[1].speciesName()).isEqualTo("Pike");
    }

    @Test
    void getUserCatches_shouldReturnUserCatches() throws Exception {

        // Create a mock User object
        User mockUser = new User();
        mockUser.setId(42L);
        mockUser.setUsername("testuser");
        mockUser.setRole(ROLE_USER);

        // Create authentication with the mock user
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.when(catchService.getCatchesByUser(42L))
                .thenReturn(mockedCatchList);

        MvcResult result = mockMvc.perform(get("/api/catch/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CatchResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CatchResponse[].class
        );

        assertThat(responses).hasSize(2);
        assertThat(responses[0].userId()).isEqualTo(42L);
        assertThat(responses[1].userId()).isEqualTo(43L);
        assertThat(responses[0].speciesName()).isEqualTo("Trout");
        assertThat(responses[1].speciesName()).isEqualTo("Pike");

        SecurityContextHolder.clearContext();
    }

    @Test
    void getCatchById_shouldReturnNotFound_whenCatchDoesNotExist() throws Exception {
        Long nonExistentId = 999L;

        Mockito.when(catchService.getCatchById(nonExistentId))
                .thenThrow(new CatchNotFoundException("Catch not found"));

        mockMvc.perform(get("/api/catch/{id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCatch_shouldReturnBadRequest_whenDataIsInvalid() throws Exception {
        CatchRequest invalidRequest = new CatchRequest(
                1L,
                null,
                null,
                null,
                FishingStyle.FLY_FISHING,
                LocalDateTime.of(2024, 5, 20, 14, 15),
                null,
                null,
                false,
                "Updated description - even bigger fight!",
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(put("/api/catch/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}