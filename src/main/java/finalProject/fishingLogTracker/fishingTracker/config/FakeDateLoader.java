package finalProject.fishingLogTracker.fishingTracker.config;

import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FakeDateLoader implements CommandLineRunner {

    private final SpeciesRepository speciesRepository;
    private final AquaticRepository aquaticRepository;

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


            List<Aquatic> aquaticList = new ArrayList<>();

            aquaticList.add(new Aquatic("Alytaus ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic("Asvejos ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic("Geležinio ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic("Ignalinos ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic("Kučio ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic("Lielais ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic("Neris", AquaticType.RIVER));
            aquaticList.add(new Aquatic("Nemunas", AquaticType.RIVER));
            aquaticList.add(new Aquatic("Šventoji", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Venta", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Mūša", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Ežerėlis", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Drūkšiai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Galvė", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Vidyžiai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Rūdupis", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Širvinta", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Taujėnai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Zarasai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Kražantė", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Ilgis", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Skrunda", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Lūšiai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Bielis", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Pankūnai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Paberžė", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Mingė", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Kuršiai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Biržų ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Platelių ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Pasvalys", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Šalčius", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Pakruojo ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Vilkaviškis", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Kupiškis", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Obeliai", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Trakai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Utena", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Pavandeniai", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Mūšos upė", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Dainava", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Daugai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Klimantiškiai", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Žeimena", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Kvietiniai ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Igliai", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Vilnia", AquaticType.RIVER));
            aquaticList.add(new Aquatic( "Suvalkijos ežeras", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Vydmantai", AquaticType.LAKE));
            aquaticList.add(new Aquatic( "Pakruojo upė", AquaticType.RIVER));


        aquaticRepository.saveAll(aquaticList);

    }
}
