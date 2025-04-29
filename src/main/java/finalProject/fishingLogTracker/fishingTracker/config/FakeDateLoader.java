package finalProject.fishingLogTracker.fishingTracker.config;

import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import finalProject.fishingLogTracker.fishingTracker.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FakeDateLoader implements CommandLineRunner {

    private final SpeciesRepository speciesRepository;

    @Override
    public void run(String... args) throws Exception {


        List<Species> speciesList = List.of(
                        new Species(null, "Northern Pike", "Esox lucius"),
                        new Species(null, "European Perch", "Perca fluviatilis"),
                        new Species(null, "Zander", "Sander lucioperca"),
                        new Species(null, "Wels Catfish", "Silurus glanis"),
                        new Species(null, "Common Bream", "Abramis brama"),
                        new Species(null, "Tench", "Tinca tinca"),
                        new Species(null, "Crucian Carp", "Carassius carassius"),
                        new Species(null, "Roach", "Rutilus rutilus"),
                        new Species(null, "Bleak", "Alburnus alburnus"),
                        new Species(null, "Chub", "Squalius cephalus"),
                        new Species(null, "European Eel", "Anguilla anguilla"),
                        new Species(null, "Brown Trout", "Salmo trutta"),
                        new Species(null, "Grayling", "Thymallus thymallus"),
                        new Species(null, "Burbot", "Lota lota"),
                        new Species(null, "Weatherfish", "Misgurnus fossilis"),
                        new Species(null, "Nase", "Chondrostoma nasus"),
                        new Species(null, "Vendace", "Coregonus albula"),
                        new Species(null, "Peled", "Coregonus peled"),
                        new Species(null, "Common Carp", "Cyprinus carpio"),
                        new Species(null, "Grass Carp", "Ctenopharyngodon idella"),
                        new Species(null, "Atlantic Salmon", "Salmo salar"),  // Migratory species
                        new Species(null, "Sea Trout", "Salmo trutta trutta"),  // Found in Baltic Sea and rivers
                        new Species(null, "Flounder", "Platichthys flesus"),  // Marine species from the Baltic
                        new Species(null, "Herring", "Clupea harengus"),  // Marine species in Baltic
                        new Species(null, "Cod", "Gadus morhua"),  // Marine species from the Baltic Sea
                        new Species(null, "Baltic Sprat", "Sprattus sprattus balticus"),  // A small fish from the Baltic Sea
                        new Species(null, "European Smelt", "Osmerus eperlanus"),  // Found in Baltic Sea and lakes
                        new Species(null, "Sturgeon", "Acipenser sturio")  // Endangered, but historically in Lithuanian rivers
                );

        speciesRepository.saveAll(speciesList);


    }
}
