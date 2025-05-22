package finalProject.fishingLogTracker.fishingTracker.service;

import finalProject.fishingLogTracker.fishingTracker.dto.BaitRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.BaitResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.exception.BaitNotFoundException;
import finalProject.fishingLogTracker.fishingTracker.mapper.BaitMapper;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaitServiceTest {

    @Mock
    private BaitRepository baitRepository;

    @Mock
    private BaitMapper baitMapper;

    @InjectMocks
    private BaitService baitService;

    private Bait wormBait;
    private Bait spinnerBait;
    private BaitResponse wormResponse;
    private BaitResponse spinnerResponse;
    private BaitRequest newBaitRequest;

    @BeforeEach
    void setUp() {
        wormBait = new Bait();
        wormBait.setId(1L);
        wormBait.setBaitType(BaitType.BLOODWORM);
        wormBait.setDescription("Common earthworm");

        spinnerBait = new Bait();
        spinnerBait.setId(2L);
        spinnerBait.setBaitType(BaitType.SPINNER);
        spinnerBait.setDescription("Silver spinner");

        wormResponse = new BaitResponse(1L, BaitType.BLOODWORM, "Common earthworm");
        spinnerResponse = new BaitResponse(2L, BaitType.SPINNER, "Silver spinner");

        newBaitRequest = new BaitRequest(BaitType.FLY, "Artificial fly");
    }

    @Test
    void shouldGetAllBaits() {
        when(baitRepository.findAll()).thenReturn(List.of(wormBait, spinnerBait));
        when(baitMapper.toBaitResponse(wormBait)).thenReturn(wormResponse);
        when(baitMapper.toBaitResponse(spinnerBait)).thenReturn(spinnerResponse);

        List<BaitResponse> result = baitService.getAllBaits();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(wormResponse, spinnerResponse);
        verify(baitRepository, times(1)).findAll();
        verify(baitMapper, times(2)).toBaitResponse(any(Bait.class));
    }

    @Test
    void shouldAddNewBait() {
        Bait flyBait = new Bait();
        flyBait.setId(3L);
        flyBait.setBaitType(BaitType.FLY);
        flyBait.setDescription("Artificial fly");

        BaitResponse flyResponse = new BaitResponse(3L, BaitType.FLY, "Artificial fly");

        when(baitMapper.toBait(newBaitRequest)).thenReturn(flyBait);
        when(baitRepository.save(flyBait)).thenReturn(flyBait);
        when(baitMapper.toBaitResponse(flyBait)).thenReturn(flyResponse);

        BaitResponse result = baitService.addNewBait(newBaitRequest);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.baitType()).isEqualTo(BaitType.FLY);
        assertThat(result.description()).isEqualTo("Artificial fly");

        verify(baitMapper, times(1)).toBait(newBaitRequest);
        verify(baitRepository, times(1)).save(flyBait);
        verify(baitMapper, times(1)).toBaitResponse(flyBait);
    }

    @Test
    void shouldReturnEmptyListWhenNoBaits() {
        when(baitRepository.findAll()).thenReturn(List.of());

        List<BaitResponse> result = baitService.getAllBaits();

        assertThat(result).isEmpty();
        verify(baitRepository, times(1)).findAll();
        verify(baitMapper, never()).toBaitResponse(any());
    }

    @Test
    void shouldUpdateBait() {
        // Arrange
        BaitRequest updateRequest = new BaitRequest(BaitType.SPINNER, "Updated spinner");
        Bait updatedBait = new Bait();
        updatedBait.setId(1L);
        updatedBait.setBaitType(BaitType.SPINNER);
        updatedBait.setDescription("Updated spinner");
        BaitResponse updatedResponse = new BaitResponse(1L, BaitType.SPINNER, "Updated spinner");

        when(baitRepository.findById(1L)).thenReturn(Optional.of(wormBait));
        when(baitMapper.toBait(updateRequest)).thenReturn(updatedBait);
        when(baitRepository.save(updatedBait)).thenReturn(updatedBait);
        when(baitMapper.toBaitResponse(updatedBait)).thenReturn(updatedResponse);

        // Act
        BaitResponse result = baitService.updateBait(1L, updateRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.baitType()).isEqualTo(BaitType.SPINNER);
        assertThat(result.description()).isEqualTo("Updated spinner");

        verify(baitRepository).findById(1L);
        verify(baitMapper).toBait(updateRequest);
        verify(baitRepository).save(updatedBait);
        verify(baitMapper).toBaitResponse(updatedBait);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentBait() {
        // Arrange
        when(baitRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> baitService.updateBait(999L, newBaitRequest))
                .isInstanceOf(BaitNotFoundException.class)
                .hasMessageContaining("Bait not found with id: 999");

        verify(baitRepository).findById(999L);
        verify(baitMapper, never()).toBait(any());
        verify(baitRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBait() {
        // Arrange
        when(baitRepository.existsById(1L)).thenReturn(true);

        // Act
        baitService.deleteBait(1L);

        // Assert
        verify(baitRepository).existsById(1L);
        verify(baitRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentBait() {
        // Arrange
        when(baitRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> baitService.deleteBait(999L))
                .isInstanceOf(BaitNotFoundException.class)
                .hasMessageContaining("Bait not found with id: 999");

        verify(baitRepository).existsById(999L);
        verify(baitRepository, never()).deleteById(any());
    }
}