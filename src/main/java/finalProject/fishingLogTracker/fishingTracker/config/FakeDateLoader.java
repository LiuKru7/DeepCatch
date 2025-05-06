package finalProject.fishingLogTracker.fishingTracker.config;

import finalProject.fishingLogTracker.fishingTracker.entity.*;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.repository.AquaticRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.BaitRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.CatchRepository;
import finalProject.fishingLogTracker.fishingTracker.repository.SpeciesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FakeDateLoader implements CommandLineRunner {

    private final SpeciesRepository speciesRepository;
    private final AquaticRepository aquaticRepository;
    private final BaitRepository baitRepository;
    private final CatchRepository catchRepository;

    @Override
    public void run(String... args) throws Exception {


        List<Species> speciesList = List.of(
                new Species( "Northern Pike", "Esox lucius"),
                new Species( "European Perch", "Perca fluviatilis"),
                new Species( "Zander", "Sander lucioperca"),
                new Species( "Wels Catfish", "Silurus glanis"),
                new Species( "Common Bream", "Abramis brama"),
                new Species( "Tench", "Tinca tinca"),
                new Species( "Crucian Carp", "Carassius carassius"),
                new Species( "Roach", "Rutilus rutilus"),
                new Species( "Bleak", "Alburnus alburnus"),
                new Species( "Chub", "Squalius cephalus"),
                new Species( "European Eel", "Anguilla anguilla"),
                new Species( "Brown Trout", "Salmo trutta"),
                new Species( "Grayling", "Thymallus thymallus"),
                new Species( "Burbot", "Lota lota"),
                new Species( "Weatherfish", "Misgurnus fossilis"),
                new Species( "Nase", "Chondrostoma nasus"),
                new Species( "Vendace", "Coregonus albula"),
                new Species( "Peled", "Coregonus peled"),
                new Species( "Common Carp", "Cyprinus carpio"),
                new Species( "Grass Carp", "Ctenopharyngodon idella"),
                new Species( "Atlantic Salmon", "Salmo salar"),  // Migratory species
                new Species( "Sea Trout", "Salmo trutta trutta"),  // Found in Baltic Sea and rivers
                new Species( "Flounder", "Platichthys flesus"),  // Marine species from the Baltic
                new Species( "Herring", "Clupea harengus"),  // Marine species in Baltic
                new Species( "Cod", "Gadus morhua"),  // Marine species from the Baltic Sea
                new Species( "Baltic Sprat", "Sprattus sprattus balticus"),  // A small fish from the Baltic Sea
                new Species( "European Smelt", "Osmerus eperlanus"),  // Found in Baltic Sea and lakes
                new Species( "Sturgeon", "Acipenser sturio")  // Endangered, but historically in Lithuanian rivers
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
        aquaticList.add(new Aquatic("Venta", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Mūša", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Ežerėlis", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Drūkšiai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Galvė", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Vidyžiai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Rūdupis", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Širvinta", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Taujėnai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Zarasai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Kražantė", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Ilgis", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Skrunda", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Lūšiai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Bielis", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Pankūnai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Paberžė", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Mingė", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Kuršiai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Biržų ežeras", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Platelių ežeras", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Pasvalys", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Šalčius", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Pakruojo ežeras", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Vilkaviškis", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Kupiškis", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Obeliai", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Trakai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Utena", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Pavandeniai", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Mūšos upė", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Dainava", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Daugai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Klimantiškiai", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Žeimena", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Kvietiniai ežeras", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Igliai", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Vilnia", AquaticType.RIVER));
        aquaticList.add(new Aquatic("Suvalkijos ežeras", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Vydmantai", AquaticType.LAKE));
        aquaticList.add(new Aquatic("Pakruojo upė", AquaticType.RIVER));


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

        var location = Location.builder()
                .latitude(43423.9)
                .longitude(5454.1)
                .country("Lithuania")
                .region("Utena")
                .build();

        var fish = Catch.builder()
                .size(99.0)
                .fishingStyle(FishingStyle.SPINNING)
                .description("Nice fishing")
                .time(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .isReleased(true)
                .weight(8.0)
                .photoUrl("pike.png")
                .build();
        fish.setBait(baits.get(0));
        fish.setAquatic(aquaticList.get(0));
        fish.setSpecies(speciesList.get(0));
        fish.setLocation(location);

        catchRepository.save(fish);

        var fish2 = Catch.builder()
                .size(99.0)
                .fishingStyle(FishingStyle.SPINNING)
                .description("Nice fishing")
                .time(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .isReleased(true)
                .weight(8.0)
                .photoUrl("lasisa.png")
                .bait(baits.get(1))
                .species(speciesList.get(1))
                .aquatic(aquaticList.get(1))
                .build();


        catchRepository.save(fish2);

        var fish3 = Catch.builder()
                .size(99.0)
                .fishingStyle(FishingStyle.FLOAT_FISHING)
                .description("Nice fishing")
                .time(new Timestamp(System.currentTimeMillis()).toLocalDateTime())
                .isReleased(true)
                .weight(8.0)
                .photoUrl("lasisa.png")
                .bait(baits.get(1))
                .species(speciesList.get(1))
                .aquatic(aquaticList.get(1))
                .build();


        catchRepository.save(fish3);

    }
}
