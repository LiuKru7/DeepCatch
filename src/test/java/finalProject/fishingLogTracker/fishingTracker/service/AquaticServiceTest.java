package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.exception.AquaticNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.AquaticMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AquaticServiceTest {

    @Mock
    private AquaticRepository aquaticRepository;

    @Mock
    private AquaticMapper aquaticMapper;

    @InjectMocks
    private AquaticService aquaticService;

    private Aquatic aquatic1;
    private Aquatic aquatic2;
    private AquaticResponse aquaticResponse1;
    private AquaticResponse aquaticResponse2;

    @BeforeEach
    void setUp() {
        aquatic1 = new Aquatic();
        aquatic1.setId(1L);
        aquatic1.setName("Lake");
        aquatic1.setAquaticType(AquaticType.LAKE);

        aquatic2 = new Aquatic();
        aquatic2.setId(2L);
        aquatic2.setName("River");
        aquatic2.setAquaticType(AquaticType.RIVER);

        aquaticResponse1 = new AquaticResponse(1L, "Lake", AquaticType.LAKE);
        aquaticResponse2 = new AquaticResponse(2L, "River", AquaticType.RIVER);
    }

    @Test
    void getAllAquatics_ShouldReturnAquaticList() {
        List<Aquatic> testList = Arrays.asList(aquatic1, aquatic2);
        when(aquaticRepository.findAll()).thenReturn(testList);
        when(aquaticMapper.toAquaticResponse(aquatic1)).thenReturn(aquaticResponse1);
        when(aquaticMapper.toAquaticResponse(aquatic2)).thenReturn(aquaticResponse2);

        List<AquaticResponse> result = aquaticService.getAllAquatics();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Lake");
        assertThat(result.get(1).name()).isEqualTo("River");

        verify(aquaticRepository).findAll();
        verify(aquaticMapper).toAquaticResponse(aquatic1);
        verify(aquaticMapper).toAquaticResponse(aquatic2);
    }

    @Test
    void addNewAquatic_ShouldReturnNewAquatic() {
        AquaticRequest request = new AquaticRequest("New Lake", AquaticType.LAKE);
        Aquatic newAquatic = new Aquatic();
        newAquatic.setName("New Lake");
        newAquatic.setAquaticType(AquaticType.LAKE);
        AquaticResponse expectedResponse = new AquaticResponse(3L, "New Lake", AquaticType.LAKE);

        when(aquaticMapper.toAquatic(request)).thenReturn(newAquatic);
        when(aquaticRepository.save(newAquatic)).thenReturn(newAquatic);
        when(aquaticMapper.toAquaticResponse(newAquatic)).thenReturn(expectedResponse);

        AquaticResponse result = aquaticService.addNewAquatic(request);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("New Lake");
        verify(aquaticRepository).save(newAquatic);
    }

    @Test
    void updateAquatic_ShouldReturnUpdatedAquatic() {
        Long id = 1L;
        AquaticRequest request = new AquaticRequest("Updated Lake", AquaticType.SEA);
        Aquatic updatedAquatic = new Aquatic();
        updatedAquatic.setId(id);
        updatedAquatic.setName("Updated Lake");
        updatedAquatic.setAquaticType(AquaticType.SEA);
        AquaticResponse expectedResponse = new AquaticResponse(id, "Updated Lake", AquaticType.SEA);

        when(aquaticRepository.findById(id)).thenReturn(java.util.Optional.of(aquatic1));
        when(aquaticMapper.toAquatic(request)).thenReturn(updatedAquatic);
        when(aquaticRepository.save(updatedAquatic)).thenReturn(updatedAquatic);
        when(aquaticMapper.toAquaticResponse(updatedAquatic)).thenReturn(expectedResponse);

        AquaticResponse result = aquaticService.updateAquatic(id, request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Updated Lake");
        verify(aquaticRepository).findById(id);
        verify(aquaticRepository).save(updatedAquatic);
    }

    @Test
    void updateAquatic_ShouldThrowException_WhenAquaticNotFound() {
        Long id = 999L;
        AquaticRequest request = new AquaticRequest("Updated Lake", AquaticType.SEA);

        when(aquaticRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(AquaticNotFoundException.class, () -> aquaticService.updateAquatic(id, request));
        verify(aquaticRepository).findById(id);
    }

    @Test
    void deleteAquatic_ShouldDeleteAquatic() {
        Long id = 1L;
        when(aquaticRepository.existsById(id)).thenReturn(true);

        aquaticService.deleteAquatic(id);

        verify(aquaticRepository).existsById(id);
        verify(aquaticRepository).deleteById(id);
    }

    @Test
    void deleteAquatic_ShouldThrowException_WhenAquaticNotFound() {
        Long id = 999L;
        when(aquaticRepository.existsById(id)).thenReturn(false);

        assertThrows(AquaticNotFoundException.class, () -> aquaticService.deleteAquatic(id));
        verify(aquaticRepository).existsById(id);
    }

    @Test
    void addNewAquatic_ShouldThrowException_WhenRequestIsInvalid() {
        AquaticRequest invalidRequest = new AquaticRequest(null, null);
        Aquatic invalidAquatic = new Aquatic();
        invalidAquatic.setName(null);
        invalidAquatic.setAquaticType(null);

        when(aquaticMapper.toAquatic(invalidRequest)).thenReturn(invalidAquatic);
        when(aquaticRepository.save(invalidAquatic)).thenThrow(new IllegalArgumentException("Invalid aquatic data"));

        assertThrows(IllegalArgumentException.class, () -> aquaticService.addNewAquatic(invalidRequest));
        verify(aquaticMapper).toAquatic(invalidRequest);
    }

    @Test
    void updateAquatic_ShouldThrowException_WhenRequestDataIsInvalid() {
        Long id = 1L;
        AquaticRequest invalidRequest = new AquaticRequest("", null);
        Aquatic invalidAquatic = new Aquatic();
        invalidAquatic.setId(id);
        invalidAquatic.setName("");
        invalidAquatic.setAquaticType(null);

        when(aquaticRepository.findById(id)).thenReturn(java.util.Optional.of(aquatic1));
        when(aquaticMapper.toAquatic(invalidRequest)).thenReturn(invalidAquatic);
        when(aquaticRepository.save(invalidAquatic))
                .thenThrow(new IllegalArgumentException("Aquatic name cannot be empty"));

        assertThrows(IllegalArgumentException.class, () -> aquaticService.updateAquatic(id, invalidRequest));
        verify(aquaticRepository).findById(id);
        verify(aquaticMapper).toAquatic(invalidRequest);
    }
}