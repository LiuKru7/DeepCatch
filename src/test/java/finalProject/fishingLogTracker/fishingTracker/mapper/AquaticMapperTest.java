package finalProject.fishingLogTracker.fishingTracker.mapper;

import finalProject.fishingLogTracker.fishingTracker.dto.AquaticRequest;
import finalProject.fishingLogTracker.fishingTracker.dto.AquaticResponse;
import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class AquaticMapperTest {

    private final AquaticMapper mapper = Mappers.getMapper(AquaticMapper.class);

    @Test
    void testToAquaticResponse() {
        Aquatic aquatic = new Aquatic("Luknas", AquaticType.LAKE);
        aquatic.setId(1L);

        AquaticResponse response = mapper.toAquaticResponse(aquatic);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Luknas");
        assertThat(response.aquaticType()).isEqualTo(AquaticType.LAKE);
    }

    @Test
    void testToAquatic() {
        AquaticRequest request = new AquaticRequest("Nemunas", AquaticType.RIVER);

        Aquatic aquatic = mapper.toAquatic(request);

        assertThat(aquatic.getName()).isEqualTo("Nemunas");
        assertThat(aquatic.getAquaticType()).isEqualTo(AquaticType.RIVER);
    }
}
