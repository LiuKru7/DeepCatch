package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.SpeciesResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import finalProject.fishingLogTracker.fishingTracker.exception.SpeciesNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.SpeciesMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.SpeciesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpeciesServiceTest {

    @Mock
    private SpeciesRepository speciesRepository;

    @Mock
    private SpeciesMapper speciesMapper;

    @InjectMocks
    private SpeciesService speciesService;

    private Species species1;
    private Species species2;
    private SpeciesResponse speciesResponse1;
    private SpeciesResponse speciesResponse2;

    @BeforeEach
    void setUp() {
        species1 = new Species();
        species1.setId(1L);
        species1.setName("Catfish");
        species1.setLatinName("katinelis didziulis");

        species2 = new Species();
        species2.setId(2L);
        species2.setName("Carp");
        species2.setLatinName("karpis rafailas");

        speciesResponse1 = new SpeciesResponse(1L, "Catfish", "katinelis didziulis");
        speciesResponse2 = new SpeciesResponse(2L, "Carp", "karpis rafailas");
    }

    @Test
    void getAllSpecies_ShouldReturnSpeciesList() {
        List<Species> testList = Arrays.asList(species1, species2);
        when(speciesRepository.findAll()).thenReturn(testList);
        when(speciesMapper.toSpeciesResponse(species1)).thenReturn(speciesResponse1);
        when(speciesMapper.toSpeciesResponse(species2)).thenReturn(speciesResponse2);

        List<SpeciesResponse> result = speciesService.getAllSpecies();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name()).isEqualTo("Catfish");
        assertThat(result.get(1).name()).isEqualTo("Carp");

        verify(speciesRepository).findAll();
        verify(speciesMapper).toSpeciesResponse(species1);
        verify(speciesMapper).toSpeciesResponse(species2);
    }

    @Test
    void getSpeciesById_ShouldReturnSpecies() {
        when(speciesRepository.findById(1L)).thenReturn(java.util.Optional.of(species1));
        when(speciesMapper.toSpeciesResponse(species1)).thenReturn(speciesResponse1);

        SpeciesResponse result = speciesService.getSpeciesById(1L);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Catfish");
        verify(speciesRepository).findById(1L);
        verify(speciesMapper).toSpeciesResponse(species1);
    }

    @Test
    void getSpeciesById_ShouldThrowException_WhenSpeciesNotFound() {
        when(speciesRepository.findById(999L)).thenReturn(java.util.Optional.empty());

        assertThrows(SpeciesNotFoundException.class, () -> speciesService.getSpeciesById(999L));
        verify(speciesRepository).findById(999L);
    }

    @Test
    void addNewSpecies_ShouldReturnNewSpecies() {
        SpeciesRequest request = new SpeciesRequest("New Fish", "New Latin Name");
        Species newSpecies = new Species();
        newSpecies.setName("New Fish");
        newSpecies.setLatinName("New Latin Name");
        SpeciesResponse expectedResponse = new SpeciesResponse(3L, "New Fish", "New Latin Name");

        when(speciesMapper.toSpecies(request)).thenReturn(newSpecies);
        when(speciesRepository.save(newSpecies)).thenReturn(newSpecies);
        when(speciesMapper.toSpeciesResponse(newSpecies)).thenReturn(expectedResponse);

        SpeciesResponse result = speciesService.addNewSpecies(request);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("New Fish");
        verify(speciesRepository).save(newSpecies);
    }

    @Test
    void updateSpecies_ShouldReturnUpdatedSpecies() {
        Long id = 1L;
        SpeciesRequest request = new SpeciesRequest("Updated Fish", "Updated Latin Name");
        Species updatedSpecies = new Species();
        updatedSpecies.setId(id);
        updatedSpecies.setName("Updated Fish");
        updatedSpecies.setLatinName("Updated Latin Name");
        SpeciesResponse expectedResponse = new SpeciesResponse(id, "Updated Fish", "Updated Latin Name");

        when(speciesRepository.findById(id)).thenReturn(java.util.Optional.of(species1));
        when(speciesMapper.toSpecies(request)).thenReturn(updatedSpecies);
        when(speciesRepository.save(updatedSpecies)).thenReturn(updatedSpecies);
        when(speciesMapper.toSpeciesResponse(updatedSpecies)).thenReturn(expectedResponse);

        SpeciesResponse result = speciesService.updateSpecies(id, request);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(id);
        assertThat(result.name()).isEqualTo("Updated Fish");
        verify(speciesRepository).findById(id);
        verify(speciesRepository).save(updatedSpecies);
    }

    @Test
    void updateSpecies_ShouldThrowException_WhenSpeciesNotFound() {
        Long id = 999L;
        SpeciesRequest request = new SpeciesRequest("Updated Fish", "Updated Latin Name");

        when(speciesRepository.findById(id)).thenReturn(java.util.Optional.empty());

        assertThrows(SpeciesNotFoundException.class, () -> speciesService.updateSpecies(id, request));
        verify(speciesRepository).findById(id);
    }

    @Test
    void deleteSpecies_ShouldDeleteSpecies() {
        Long id = 1L;
        when(speciesRepository.existsById(id)).thenReturn(true);

        speciesService.deleteSpecies(id);

        verify(speciesRepository).existsById(id);
        verify(speciesRepository).deleteById(id);
    }

    @Test
    void deleteSpecies_ShouldThrowException_WhenSpeciesNotFound() {
        Long id = 999L;
        when(speciesRepository.existsById(id)).thenReturn(false);

        assertThrows(SpeciesNotFoundException.class, () -> speciesService.deleteSpecies(id));
        verify(speciesRepository).existsById(id);
    }

    @Test
    void addNewSpecies_ShouldThrowException_WhenRequestIsInvalid() {
        // Test with null values
        SpeciesRequest invalidRequest = new SpeciesRequest(null, null);
        Species invalidSpecies = new Species();
        invalidSpecies.setName(null);
        invalidSpecies.setLatinName(null);

        when(speciesMapper.toSpecies(invalidRequest)).thenReturn(invalidSpecies);
        when(speciesRepository.save(invalidSpecies)).thenThrow(new IllegalArgumentException("Invalid species data"));

        assertThrows(IllegalArgumentException.class, () -> speciesService.addNewSpecies(invalidRequest));
        verify(speciesMapper).toSpecies(invalidRequest);
    }

    @Test
    void updateSpecies_ShouldThrowException_WhenRequestDataIsInvalid() {
        Long id = 1L;
        // Test with empty strings
        SpeciesRequest invalidRequest = new SpeciesRequest("", "");
        Species invalidSpecies = new Species();
        invalidSpecies.setId(id);
        invalidSpecies.setName("");
        invalidSpecies.setLatinName("");

        when(speciesRepository.findById(id)).thenReturn(java.util.Optional.of(species1));
        when(speciesMapper.toSpecies(invalidRequest)).thenReturn(invalidSpecies);
        when(speciesRepository.save(invalidSpecies))
                .thenThrow(new IllegalArgumentException("Species name cannot be empty"));

        assertThrows(IllegalArgumentException.class, () -> speciesService.updateSpecies(id, invalidRequest));
        verify(speciesRepository).findById(id);
        verify(speciesMapper).toSpecies(invalidRequest);
    }
}