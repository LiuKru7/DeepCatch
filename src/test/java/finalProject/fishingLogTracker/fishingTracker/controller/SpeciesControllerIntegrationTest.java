package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.exception.SpeciesNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.service.SpeciesService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SpeciesControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpeciesService speciesService;

    @Test
    void getAllSpecies_shouldReturnListOfSpecies() throws Exception {
        List<SpeciesResponse> mockResponse = List.of(
                new SpeciesResponse(1L, "Catfish", "kitinelis didziulis"),
                new SpeciesResponse(2L, "Carp", "karpis rafailas")
        );
        Mockito.when(speciesService.getAllSpecies()).thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/species")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        SpeciesResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SpeciesResponse[].class
        );

        assertThat(responses).hasSize(2);
        assertThat(responses[0].name()).isEqualTo("Catfish");
        assertThat(responses[0].latinName()).isEqualTo("kitinelis didziulis");
        assertThat(responses[1].name()).isEqualTo("Carp");
        assertThat(responses[1].latinName()).isEqualTo("karpis rafailas");
    }

    @Test
    void addNewSpecies_shouldReturnCreatedSpecies() throws Exception {
        SpeciesRequest speciesRequest = new SpeciesRequest("White Perch", "percas albinosas");
        SpeciesResponse mockResponse = new SpeciesResponse(3L, "White Perch", "percas albinosas");

        Mockito.when(speciesService.addNewSpecies(Mockito.any(SpeciesRequest.class)))
                .thenReturn(mockResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/species")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(speciesRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        SpeciesResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SpeciesResponse.class
        );

        assertThat(response.id()).isEqualTo(3L);
        assertThat(response.name()).isEqualTo("White Perch");
        assertThat(response.latinName()).isEqualTo("percas albinosas");
    }

    @Test
    void updateSpecies_shouldReturnUpdatedSpecies() throws Exception {
        Long id = 3L;
        SpeciesRequest updateRequest = new SpeciesRequest("Updated white Perch", "percas albinosas updeitas");
        SpeciesResponse updatedResponse = new SpeciesResponse(id, "Updated white Perch", "percas albinosas updeitas");

        Mockito.when(speciesService.updateSpecies(Mockito.eq(id), Mockito.any(SpeciesRequest.class)))
                .thenReturn(updatedResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/species/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        SpeciesResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SpeciesResponse.class
        );

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("Updated white Perch");
        assertThat(response.latinName()).isEqualTo("percas albinosas updeitas");

    }

    @Test
    void deleteSpecies_shouldDeleteSpeciesById() throws Exception {
        Long id = 3L;

        Mockito.doNothing().when(speciesService).deleteSpecies(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/species/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(speciesService, Mockito.times(1)).deleteSpecies(id);
    }

    @Test
    void addNewSpecies_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        SpeciesRequest invalidRequest = new SpeciesRequest("", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/species")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateSpecies_shouldReturnNotFound_whenSpeciesDoesNotExist() throws Exception {
        Long badId = 999L;

        SpeciesRequest updateRequest = new SpeciesRequest("Crazy Fish", "carpis pamiselis");

        Mockito.when(speciesService.updateSpecies(Mockito.eq(badId), Mockito.any(SpeciesRequest.class)))
                .thenThrow(new SpeciesNotFoundException("Species not found with id: " + badId));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/species/{id}", badId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Species not found with id: " + badId));
    }

}