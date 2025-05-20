package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import finalProject.fishingLogTracker.fishingTracker.service.SpeciesService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SpeciesControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private SpeciesService speciesService;

        @Autowired
        private CatchRepository catchRepository;

        @BeforeEach
        void setUp() {
                // Clean up any existing data
                catchRepository.deleteAll(); // Delete catches first
                speciesService.getAllSpecies().forEach(species -> speciesService.deleteSpecies(species.id()));
        }

        @AfterEach
        void tearDown() {
                // Clean up after each test
                catchRepository.deleteAll(); // Delete catches first
                speciesService.getAllSpecies().forEach(species -> speciesService.deleteSpecies(species.id()));
        }

        @Test
        @WithMockUser(roles = "USER")
        void getAllSpecies_shouldReturnListOfSpecies() throws Exception {
                // Given
                SpeciesRequest species1 = new SpeciesRequest("Catfish", "kitinelis didziulis");
                SpeciesRequest species2 = new SpeciesRequest("Carp", "karpis rafailas");
                speciesService.addNewSpecies(species1);
                speciesService.addNewSpecies(species2);

                // When
                MvcResult result = mockMvc.perform(get("/api/species")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                SpeciesResponse[] responses = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                SpeciesResponse[].class);

                assertThat(responses).hasSize(2);
                assertThat(responses).extracting("name").containsExactlyInAnyOrder("Catfish", "Carp");
                assertThat(responses).extracting("latinName").containsExactlyInAnyOrder("kitinelis didziulis",
                                "karpis rafailas");
        }

        @Test
        @WithMockUser(roles = "USER")
        void addNewSpecies_shouldReturnCreatedSpecies() throws Exception {
                // Given
                SpeciesRequest speciesRequest = new SpeciesRequest("White Perch", "percas albinosas");

                // When
                MvcResult result = mockMvc.perform(post("/api/species")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(speciesRequest)))
                                .andExpect(status().isCreated())
                                .andReturn();

                // Then
                SpeciesResponse response = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                SpeciesResponse.class);

                assertThat(response.name()).isEqualTo("White Perch");
                assertThat(response.latinName()).isEqualTo("percas albinosas");

                // Verify database state
                assertThat(speciesService.getAllSpecies())
                                .hasSize(1)
                                .first()
                                .satisfies(savedSpecies -> {
                                        assertThat(savedSpecies.name()).isEqualTo("White Perch");
                                        assertThat(savedSpecies.latinName()).isEqualTo("percas albinosas");
                                });
        }

        @Test
        @WithMockUser(roles = "USER")
        void updateSpecies_shouldReturnUpdatedSpecies() throws Exception {
                // Given
                SpeciesRequest initialRequest = new SpeciesRequest("White Perch", "percas albinosas");
                SpeciesResponse createdSpecies = speciesService.addNewSpecies(initialRequest);

                SpeciesRequest updateRequest = new SpeciesRequest("Updated white Perch", "percas albinosas updeitas");

                // When
                MvcResult result = mockMvc.perform(put("/api/species/{id}", createdSpecies.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                                .andExpect(status().isOk())
                                .andReturn();

                // Then
                SpeciesResponse response = objectMapper.readValue(
                                result.getResponse().getContentAsString(),
                                SpeciesResponse.class);

                assertThat(response.id()).isEqualTo(createdSpecies.id());
                assertThat(response.name()).isEqualTo("Updated white Perch");
                assertThat(response.latinName()).isEqualTo("percas albinosas updeitas");

                // Verify database state
                assertThat(speciesService.getAllSpecies())
                                .hasSize(1)
                                .first()
                                .satisfies(updatedSpecies -> {
                                        assertThat(updatedSpecies.name()).isEqualTo("Updated white Perch");
                                        assertThat(updatedSpecies.latinName()).isEqualTo("percas albinosas updeitas");
                                });
        }

        @Test
        @WithMockUser(roles = "USER")
        void deleteSpecies_shouldDeleteSpeciesById() throws Exception {
                // Given
                SpeciesRequest speciesRequest = new SpeciesRequest("White Perch", "percas albinosas");
                SpeciesResponse createdSpecies = speciesService.addNewSpecies(speciesRequest);

                // When
                mockMvc.perform(delete("/api/species/{id}", createdSpecies.id())
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                // Then
                assertThat(speciesService.getAllSpecies()).isEmpty();
        }

        @Test
        @WithMockUser(roles = "USER")
        void addNewSpecies_shouldReturnBadRequest_whenInvalidInput() throws Exception {
                // Given
                SpeciesRequest invalidRequest = new SpeciesRequest("", "");

                // When/Then
                mockMvc.perform(post("/api/species")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(roles = "USER")
        void updateSpecies_shouldReturnNotFound_whenSpeciesDoesNotExist() throws Exception {
                // Given
                Long nonExistentId = 999L;
                SpeciesRequest updateRequest = new SpeciesRequest("Crazy Fish", "carpis pamiselis");

                // When/Then
                mockMvc.perform(put("/api/species/{id}", nonExistentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateRequest)))
                                .andExpect(status().isNotFound());
        }
}