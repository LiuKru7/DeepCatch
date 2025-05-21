package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BaitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BaitRepository baitRepository;

    @Autowired
    private CatchRepository catchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Delete catches first to handle foreign key constraints
        catchRepository.deleteAll();
        baitRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Delete catches first to handle foreign key constraints
        catchRepository.deleteAll();
        baitRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllBaits_shouldReturnListOfBaits() throws Exception {
        // Given
        Bait worm = new Bait();
        worm.setBaitType(BaitType.BLOODWORM);
        worm.setDescription("Common earthworm");

        Bait spinner = new Bait();
        spinner.setBaitType(BaitType.SPINNER);
        spinner.setDescription("Silver spinner");

        baitRepository.saveAll(List.of(worm, spinner));

        // When
        MvcResult result = mockMvc.perform(get("/api/bait")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BaitResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BaitResponse[].class);

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("baitType").containsExactlyInAnyOrder(BaitType.BLOODWORM, BaitType.SPINNER);
        assertThat(responses).extracting("description").containsExactlyInAnyOrder("Common earthworm", "Silver spinner");
    }

    @Test
    @WithMockUser(roles = "USER")
    void addNewBait_shouldReturnCreatedBait() throws Exception {
        // Given
        BaitRequest request = new BaitRequest(BaitType.FLY, "Artificial fly");

        // When
        MvcResult result = mockMvc.perform(post("/api/bait")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        BaitResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BaitResponse.class);

        assertThat(response.baitType()).isEqualTo(BaitType.FLY);
        assertThat(response.description()).isEqualTo("Artificial fly");

        // Verify database state
        List<Bait> baits = baitRepository.findAll();
        assertThat(baits).hasSize(1);
        assertThat(baits.get(0).getBaitType()).isEqualTo(BaitType.FLY);
        assertThat(baits.get(0).getDescription()).isEqualTo("Artificial fly");
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateBait_shouldReturnUpdatedBait() throws Exception {
        // Given
        Bait bait = new Bait();
        bait.setBaitType(BaitType.EARTHWORM);
        bait.setDescription("Old description");
        Bait savedBait = baitRepository.save(bait);

        BaitRequest updateRequest = new BaitRequest(BaitType.EARTHWORM, "Updated description");

        // When
        MvcResult result = mockMvc.perform(put("/api/bait/{id}", savedBait.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BaitResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BaitResponse.class);

        assertThat(response.id()).isEqualTo(savedBait.getId());
        assertThat(response.baitType()).isEqualTo(BaitType.EARTHWORM);
        assertThat(response.description()).isEqualTo("Updated description");

        // Verify database state
        Bait updatedBait = baitRepository.findById(savedBait.getId()).orElseThrow();
        assertThat(updatedBait.getBaitType()).isEqualTo(BaitType.EARTHWORM);
        assertThat(updatedBait.getDescription()).isEqualTo("Updated description");
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteBait_shouldDeleteBaitById() throws Exception {
        // Given
        Bait bait = new Bait();
        bait.setBaitType(BaitType.EARTHWORM);
        bait.setDescription("Bait to delete");
        Bait savedBait = baitRepository.save(bait);

        // When
        mockMvc.perform(delete("/api/bait/{id}", savedBait.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        assertThat(baitRepository.findAll()).isEmpty();
    }

    @Test
    @WithMockUser(roles = "USER")
    void addNewBait_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        // Given
        String invalidJson = "{\"baitType\": null, \"description\": \"\"}";

        // When/Then
        mockMvc.perform(post("/api/bait")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.baitType").value("Bait type must not be null"))
                .andExpect(jsonPath("$.description").value("Description must not be blank"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateBait_shouldReturnNotFound_whenBaitDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        BaitRequest updateRequest = new BaitRequest(BaitType.EARTHWORM, "Non-existent bait");

        // When/Then
        mockMvc.perform(put("/api/bait/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}