package finalProject.fishingLogTracker.fishingTracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaitControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BaitRepository baitRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        baitRepository.deleteAll();
        
        Bait worm = new Bait();
        worm.setBaitType(BaitType.WORM);
        worm.setDescription("Common earthworm");
        
        Bait spinner = new Bait();
        spinner.setBaitType(BaitType.SPINNER);
        spinner.setDescription("Silver spinner");
        
        baitRepository.saveAll(List.of(worm, spinner));
    }

    @Test
    void shouldGetAllBaits() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/bait")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BaitResponse[] responses = objectMapper.readValue(
                result.getResponse().getContentAsString(), 
                BaitResponse[].class);
        
        assertThat(responses).hasSize(2);
        assertThat(responses[0].baitType()).isEqualTo(BaitType.WORM);
        assertThat(responses[0].description()).isEqualTo("Common earthworm");
        assertThat(responses[1].baitType()).isEqualTo(BaitType.SPINNER);
        assertThat(responses[1].description()).isEqualTo("Silver spinner");
    }

    @Test
    void shouldAddNewBait() throws Exception {
        BaitRequest request = new BaitRequest(BaitType.FLY, "Artificial fly");
        
        mockMvc.perform(post("/api/bait")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.baitType").value("FLY"))
                .andExpect(jsonPath("$.description").value("Artificial fly"));
        
        List<Bait> baits = baitRepository.findAll();
        assertThat(baits).hasSize(3);
        assertThat(baits.stream()
                .filter(b -> b.getBaitType() == BaitType.FLY)
                .findFirst()
                .orElseThrow()
                .getDescription()).isEqualTo("Artificial fly");
    }
    
    @Test
    void shouldReturnBadRequestWhenInvalidRequest() throws Exception {
        String invalidJson = "{\"baitType\": \"INVALID_TYPE\", \"description\": \"Test\"}";
        
        mockMvc.perform(post("/api/bait")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}