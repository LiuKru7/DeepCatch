package finalProject.fishingLogTracker.fishingTracker.config;

import finalProject.fishingLogTracker.fishingTracker.entity.Aquatic;
import finalProject.fishingLogTracker.fishingTracker.entity.Bait;
import finalProject.fishingLogTracker.fishingTracker.entity.Species;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
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
    private final BaitRepository baitRepository;

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

        List<Bait> baits = List.of(
                new Bait(BaitType.WORM, "Universalus sliekas"),
                new Bait(BaitType.GRUB, "Didelis dzikas"),
                new Bait(BaitType.SPINNER, "Raudona blyžgė"),
                new Bait(BaitType.MINNOW, "Žalias vobleris"),
                new Bait(BaitType.SHRIMP, "Maža krevetė"),
                new Bait(BaitType.FLY, "Juoda musė"),
                new Bait(BaitType.CHEESE, "Brandintas sūris"),
                new Bait(BaitType.MOTH, "Naktinė musė su raštais"),
                new Bait(BaitType.CRAB, "Mažas krabas"),
                new Bait(BaitType.WAXWORM, "Didelis vaškas"),
                new Bait(BaitType.WORM, "Rudasis sliekas"),
                new Bait(BaitType.GRUB, "Baltas dzikas"),
                new Bait(BaitType.SPINNER, "Sidabrinė blyžgė"),
                new Bait(BaitType.MINNOW, "Mėlynas vobleris"),
                new Bait(BaitType.SHRIMP, "Didelė krevetė"),
                new Bait(BaitType.FLY, "Geltona musė"),
                new Bait(BaitType.CHEESE, "Šviežias sūris"),
                new Bait(BaitType.MOTH, "Maža naktinė musė"),
                new Bait(BaitType.CRAB, "Raudonas krabas"),
                new Bait(BaitType.WAXWORM, "Mažas vaškas"),
                new Bait(BaitType.WORM, "Juodas sliekas"),
                new Bait(BaitType.GRUB, "Mažas dzikas"),
                new Bait(BaitType.SPINNER, "Auksinė blyžgė"),
                new Bait(BaitType.MINNOW, "Raudonas vobleris"),
                new Bait(BaitType.SHRIMP, "Vidutinė krevetė"),
                new Bait(BaitType.FLY, "Balta musė"),
                new Bait(BaitType.CHEESE, "Pelėsinis sūris"),
                new Bait(BaitType.MOTH, "Didelė naktinė musė"),
                new Bait(BaitType.CRAB, "Žalias krabas"),
                new Bait(BaitType.WAXWORM, "Baltas vaškas"),
                new Bait(BaitType.WORM, "Mėlynas sliekas"),
                new Bait(BaitType.GRUB, "Raudonas dzikas"),
                new Bait(BaitType.SPINNER, "Žalia blyžgė"),
                new Bait(BaitType.MINNOW, "Sidabrinis vobleris"),
                new Bait(BaitType.SHRIMP, "Rožinė krevetė"),
                new Bait(BaitType.FLY, "Žalia musė"),
                new Bait(BaitType.CHEESE, "Kietas sūris"),
                new Bait(BaitType.MOTH, "Spalvota naktinė musė"),
                new Bait(BaitType.CRAB, "Didelis krabas"),
                new Bait(BaitType.WAXWORM, "Geltonas vaškas"),
                new Bait(BaitType.WORM, "Didelis sliekas"),
                new Bait(BaitType.GRUB, "Žalias dzikas"),
                new Bait(BaitType.SPINNER, "Juoda blyžgė"),
                new Bait(BaitType.MINNOW, "Geltonas vobleris"),
                new Bait(BaitType.SHRIMP, "Maža rožinė krevetė"),
                new Bait(BaitType.FLY, "Raudona musė"),
                new Bait(BaitType.CHEESE, "Minkštas sūris"),
                new Bait(BaitType.MOTH, "Smulki naktinė musė"),
                new Bait(BaitType.CRAB, "Mėlynas krabas"),
                new Bait(BaitType.WAXWORM, "Raudonas vaškas")
        );

        baitRepository.saveAll(baits);

    }
}
