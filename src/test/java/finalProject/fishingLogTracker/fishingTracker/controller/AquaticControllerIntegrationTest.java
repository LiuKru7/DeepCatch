package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
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
class AquaticControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AquaticRepository aquaticRepository;

    @Autowired
    private CatchRepository catchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Delete catches first to handle foreign key constraints
        catchRepository.deleteAll();
        aquaticRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Delete catches first to handle foreign key constraints
        catchRepository.deleteAll();
        aquaticRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllAquatics_shouldReturnListOfAquatics() throws Exception {
        // Given
        Aquatic lake = new Aquatic();
        lake.setName("Lake Superior");
        lake.setAquaticType(AquaticType.LAKE);

        Aquatic ocean = new Aquatic();
        ocean.setName("Atlantic Ocean");
        ocean.setAquaticType(AquaticType.OCEAN);

        aquaticRepository.saveAll(List.of(lake, ocean));

        // When
        MvcResult result = mockMvc.perform(get("/api/aquatic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        AquaticResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AquaticResponse[].class);

        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("name").containsExactlyInAnyOrder("Lake Superior", "Atlantic Ocean");
        assertThat(responses).extracting("aquaticType").containsExactlyInAnyOrder(AquaticType.LAKE, AquaticType.OCEAN);
    }

    @Test
    @WithMockUser(roles = "USER")
    void addNewAquatic_shouldReturnCreatedAquatic() throws Exception {
        // Given
        AquaticRequest request = new AquaticRequest("River Nile", AquaticType.RIVER);

        // When
        MvcResult result = mockMvc.perform(post("/api/aquatic")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        AquaticResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AquaticResponse.class);

        assertThat(response.name()).isEqualTo("River Nile");
        assertThat(response.aquaticType()).isEqualTo(AquaticType.RIVER);

        // Verify database state
        List<Aquatic> aquatics = aquaticRepository.findAll();
        assertThat(aquatics).hasSize(1);
        assertThat(aquatics.get(0).getName()).isEqualTo("River Nile");
        assertThat(aquatics.get(0).getAquaticType()).isEqualTo(AquaticType.RIVER);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAquatic_shouldReturnUpdatedAquatic() throws Exception {
        // Given
        Aquatic aquatic = new Aquatic();
        aquatic.setName("Old Lake");
        aquatic.setAquaticType(AquaticType.LAKE);
        Aquatic savedAquatic = aquaticRepository.save(aquatic);

        AquaticRequest updateRequest = new AquaticRequest("Updated Lake", AquaticType.LAKE);

        // When
        MvcResult result = mockMvc.perform(put("/api/aquatic/{id}", savedAquatic.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        AquaticResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AquaticResponse.class);

        assertThat(response.id()).isEqualTo(savedAquatic.getId());
        assertThat(response.name()).isEqualTo("Updated Lake");
        assertThat(response.aquaticType()).isEqualTo(AquaticType.LAKE);

        // Verify database state
        Aquatic updatedAquatic = aquaticRepository.findById(savedAquatic.getId()).orElseThrow();
        assertThat(updatedAquatic.getName()).isEqualTo("Updated Lake");
        assertThat(updatedAquatic.getAquaticType()).isEqualTo(AquaticType.LAKE);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteAquatic_shouldDeleteAquaticById() throws Exception {
        // Given
        Aquatic aquatic = new Aquatic();
        aquatic.setName("Lake to Delete");
        aquatic.setAquaticType(AquaticType.LAKE);
        Aquatic savedAquatic = aquaticRepository.save(aquatic);

        // When
        mockMvc.perform(delete("/api/aquatic/{id}", savedAquatic.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Then
        assertThat(aquaticRepository.findAll()).isEmpty();
    }

    @Test
    @WithMockUser(roles = "USER")
    void addNewAquatic_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        // Given
        String invalidJson = "{\"name\": \"\", \"aquaticType\": null}";

        // When/Then
        mockMvc.perform(post("/api/aquatic")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name must not be blank"))
                .andExpect(jsonPath("$.aquaticType").value("Aquatic type must not be null"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateAquatic_shouldReturnNotFound_whenAquaticDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        AquaticRequest updateRequest = new AquaticRequest("Non-existent Lake", AquaticType.LAKE);

        // When/Then
        mockMvc.perform(put("/api/aquatic/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }
}
