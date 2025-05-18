package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
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
}