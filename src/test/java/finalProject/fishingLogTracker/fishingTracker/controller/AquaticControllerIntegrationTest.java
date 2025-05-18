package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AquaticControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AquaticRepository aquaticRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        aquaticRepository.deleteAll();

        Aquatic lake = new Aquatic();
        lake.setName("Lake Superior");
        lake.setAquaticType(AquaticType.LAKE);

        Aquatic ocean = new Aquatic();
        ocean.setName("Atlantic Ocean");
        ocean.setAquaticType(AquaticType.OCEAN);

        aquaticRepository.saveAll(List.of(lake, ocean));
    }

    @Test
    void shouldGetAllAquatics() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/aquatic")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AquaticResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AquaticResponse[].class
        );

        assertThat(responses).hasSize(2);
        assertThat(responses[0].aquaticType()).isIn(AquaticType.LAKE, AquaticType.OCEAN);
        assertThat(responses[1].aquaticType()).isIn(AquaticType.LAKE, AquaticType.OCEAN);
    }

    @Test
    void shouldAddNewAquatic() throws Exception {
        AquaticRequest request = new AquaticRequest("River Nile", AquaticType.RIVER);

        mockMvc.perform(post("/api/aquatic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("River Nile"))
                .andExpect(jsonPath("$.type").value("RIVER"));

        List<Aquatic> aquatics = aquaticRepository.findAll();
        assertThat(aquatics).hasSize(3);
        assertThat(aquatics.stream()
                .anyMatch(a -> a.getName().equals("River Nile") && a.getAquaticType() == AquaticType.RIVER)).isTrue();
    }

    @Test
    void shouldReturnBadRequestForInvalidAquaticType() throws Exception {
        String invalidJson = "{\"name\": \"Mystery\", \"type\": \"INVALID_TYPE\"}";

        mockMvc.perform(post("/api/aquatic")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
