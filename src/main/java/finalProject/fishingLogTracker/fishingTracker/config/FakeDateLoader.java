package finalProject.fishingLogTracker.fishingTracker.config;

import finalProject.fishingLogTracker.auth.repository.UserRepository;
import finalProject.fishingLogTracker.fishingTracker.entity.*;
import finalProject.fishingLogTracker.fishingTracker.enums.AquaticType;
import finalProject.fishingLogTracker.fishingTracker.enums.BaitType;
import finalProject.fishingLogTracker.fishingTracker.enums.FishingStyle;
import finalProject.fishingLogTracker.fishingTracker.enums.FriendshipStatus;
import finalProject.fishingLogTracker.fishingTracker.repository.*;
import finalProject.fishingLogTracker.fishingTracker.service.SpeciesService;
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
        private final UserRepository userRepository;
        private final FriendshipRepository friendshipRepository;

        @Override
        public void run(String... args) throws Exception {

                List<Species> speciesList = List.of(
                                new Species("Pike", "Esox lucius"), // Lydeka
                                new Species("Perch", "Perca fluviatilis"), // Ešerys
                                new Species("Zander", "Sander lucioperca"), // Starkis
                                new Species("Wels Catfish", "Silurus glanis"), // Šamas
                                new Species("Bream", "Abramis brama"), // Karšis
                                new Species("Tench", "Tinca tinca"), // Lynas
                                new Species("Crucian Carp", "Carassius carassius"), // Karosas
                                new Species("Roach", "Rutilus rutilus"), // Kuoja
                                new Species("Bleak", "Alburnus alburnus"), // Aukšlė
                                new Species("Chub", "Squalius cephalus"), // Šapalas
                                new Species("Eel", "Anguilla anguilla"), // Ungurys
                                new Species("Trout", "Salmo trutta"), // Upėtakis
                                new Species("Grayling", "Thymallus thymallus"), // Kiršlys
                                new Species("Burbot", "Lota lota"), // Vėgėlė
                                new Species("Weatherfish", "Misgurnus fossilis"), // Vijūnas
                                new Species("Nase", "Chondrostoma nasus"), // Žiobris
                                new Species("Vendace", "Coregonus albula"), // Sykas
                                new Species("Peled", "Coregonus peled"), // Peledė
                                new Species("Carp", "Cyprinus carpio"), // Karpis
                                new Species("Grass Carp", "Ctenopharyngodon idella"), // Amūras
                                new Species("Salmon", "Salmo salar"), // Lašiša
                                new Species("Sea Trout", "Salmo trutta trutta"), // Šlakys
                                new Species("Flounder", "Platichthys flesus"), // Plekšnė
                                new Species("Herring", "Clupea harengus"), // Silkė
                                new Species("Cod", "Gadus morhua"), // Menkė
                                new Species("Baltic Sprat", "Sprattus sprattus balticus"), // Baltinė strimelė
                                new Species("Smelt", "Osmerus eperlanus"), // Stinta
                                new Species("Sturgeon", "Acipenser oxyrinchus") // Eršketas
                );

                speciesRepository.saveAll(speciesList);

                List<Aquatic> aquaticList = List.of(
                                new Aquatic("Asveja", AquaticType.LAKE), // Vilnius district
                                new Aquatic("Gavys", AquaticType.LAKE), // Ignalina district
                                new Aquatic("Dusia", AquaticType.LAKE), // Lazdijai district
                                new Aquatic("Simnas", AquaticType.LAKE), // Alytus district
                                new Aquatic("Rėkyva", AquaticType.LAKE), // Šiauliai district
                                new Aquatic("Merkys", AquaticType.RIVER), // Varėna district
                                new Aquatic("Galvė", AquaticType.LAKE), // Trakai district
                                new Aquatic("Plateliai", AquaticType.LAKE), // Plungė district
                                new Aquatic("Tauragnas", AquaticType.LAKE), // Utena district
                                new Aquatic("Drūkšiai", AquaticType.LAKE), // Ignalina district
                                new Aquatic("Ilgis", AquaticType.LAKE), // Utena district
                                new Aquatic("Aviris", AquaticType.LAKE), // Varėna district
                                new Aquatic("Latežeris", AquaticType.LAKE), // Druskininkai municipality
                                new Aquatic("Druskonis", AquaticType.LAKE), // Druskininkai municipality
                                new Aquatic("Neris", AquaticType.RIVER), // Vilnius district
                                new Aquatic("Nemunas", AquaticType.RIVER), // Kaunas district
                                new Aquatic("Šventoji", AquaticType.RIVER), // Anykščiai district
                                new Aquatic("Venta", AquaticType.RIVER), // Mažeikiai district
                                new Aquatic("Mūša", AquaticType.RIVER), // Joniškis district
                                new Aquatic("Žeimena", AquaticType.RIVER), // Švenčionys district
                                new Aquatic("Širvinta", AquaticType.RIVER), // Širvintos district
                                new Aquatic("Kražantė", AquaticType.RIVER), // Kelmė district
                                new Aquatic("Minija", AquaticType.RIVER), // Klaipėda district
                                new Aquatic("Pasvalys", AquaticType.RIVER), // Pasvalys district
                                new Aquatic("Vilkaviškis", AquaticType.RIVER), // Vilkaviškis district
                                new Aquatic("Kupiškis", AquaticType.RIVER), // Kupiškis district
                                new Aquatic("Obeliai", AquaticType.RIVER), // Rokiškis district
                                new Aquatic("Pavandenis", AquaticType.RIVER), // Telšiai district
                                new Aquatic("Mūša", AquaticType.RIVER), // Joniškis district
                                new Aquatic("Klimantiškiai", AquaticType.RIVER), // Kėdainiai district
                                new Aquatic("Igliauka", AquaticType.RIVER), // Marijampolė municipality
                                new Aquatic("Vilnia", AquaticType.RIVER), // Vilnius district
                                new Aquatic("Pakruojis", AquaticType.RIVER), // Pakruojis district
                                new Aquatic("Žuvintas", AquaticType.LAKE), // Alytus district
                                new Aquatic("Kauno Marios", AquaticType.RESERVOIR), // Kaunas district
                                new Aquatic("Kuršių Marios", AquaticType.LAGOON), // Klaipėda district
                                new Aquatic("Skirvytė", AquaticType.RIVER), // Šilutė district
                                new Aquatic("Pakalnė", AquaticType.RIVER), // Šilutė district
                                new Aquatic("Atmata", AquaticType.RIVER), // Šilutė district
                                new Aquatic("Šalčia", AquaticType.RIVER), // Šalčininkai district
                                new Aquatic("Ūla", AquaticType.RIVER), // Varėna district
                                new Aquatic("Šešupė", AquaticType.RIVER), // Marijampolė municipality
                                new Aquatic("Nevėžis", AquaticType.RIVER), // Kėdainiai district
                                new Aquatic("Jiesia", AquaticType.RIVER), // Kaunas district
                                new Aquatic("Dubysa", AquaticType.RIVER), // Raseiniai district
                                new Aquatic("Širvėna", AquaticType.LAKE), // Biržai district
                                new Aquatic("Biržai", AquaticType.LAKE), // Biržai district
                                new Aquatic("Pakruojis", AquaticType.LAKE), // Pakruojis district
                                new Aquatic("Utena", AquaticType.LAKE), // Utena district
                                new Aquatic("Trakai", AquaticType.LAKE), // Trakai district
                                new Aquatic("Vydmantai", AquaticType.LAKE), // Kretinga district
                                new Aquatic("Suvalkija", AquaticType.LAKE), // Marijampolė municipality
                                new Aquatic("Kvietiniai", AquaticType.LAKE), // Klaipėda district
                                new Aquatic("Daugai", AquaticType.LAKE), // Alytus district
                                new Aquatic("Dainava", AquaticType.LAKE), // Alytus district
                                new Aquatic("Paberžė", AquaticType.LAKE), // Kėdainiai district
                                new Aquatic("Pankūnai", AquaticType.LAKE), // Zarasai district
                                new Aquatic("Bielis", AquaticType.LAKE), // Ignalina district
                                new Aquatic("Lūšiai", AquaticType.LAKE), // Ignalina district
                                new Aquatic("Ilgis", AquaticType.LAKE), // Utena district
                                new Aquatic("Zarasai", AquaticType.LAKE), // Zarasai district
                                new Aquatic("Taujėnai", AquaticType.LAKE), // Ukmergė district
                                new Aquatic("Vidyžiai", AquaticType.LAKE), // Švenčionys district
                                new Aquatic("Geležinis", AquaticType.LAKE), // Vilnius district
                                new Aquatic("Alytus", AquaticType.LAKE), // Alytus district
                                new Aquatic("Kučas", AquaticType.LAKE), // Utena district
                                new Aquatic("Lielais", AquaticType.LAKE), // Zarasai district
                                new Aquatic("Ežerėlis", AquaticType.LAKE), // Kaunas district
                                new Aquatic("Rūdupis", AquaticType.RIVER), // Kaunas district
                                new Aquatic("Gilužis", AquaticType.LAKE), // Vilnius district
                                new Aquatic("Kraujeliai", AquaticType.LAKE), // Šiauliai district
                                new Aquatic("Juodkrantė", AquaticType.LAKE), // Neringa municipality
                                new Aquatic("Salantai", AquaticType.RIVER), // Kretinga district
                                new Aquatic("Rėkyva", AquaticType.RIVER), // Šiauliai district
                                new Aquatic("Skirvytė", AquaticType.RIVER), // Šilutė district
                                new Aquatic("Viekšniai", AquaticType.LAKE), // Mažeikiai district
                                new Aquatic("Mastis", AquaticType.LAKE), // Telšiai district
                                new Aquatic("Tverečius", AquaticType.LAKE), // Ignalina district
                                new Aquatic("Skaistgirys", AquaticType.LAKE) // Joniškis district
                );

                aquaticRepository.saveAll(aquaticList);

                List<Bait> baits = List.of(
                                new Bait(BaitType.EARTHWORM, "Sliekas"),
                                new Bait(BaitType.NIGHTCRAWLER, "Didelis naktinis sliekas"),
                                new Bait(BaitType.BLOODWORM, "Uodo trūklio lervos (matylius)"),
                                new Bait(BaitType.MAGGOT, "Musės lerva (dzikas)"),
                                new Bait(BaitType.LEECH, "Dėlė"),
                                new Bait(BaitType.MINNOW, "Maža gyva žuvelė"),
                                new Bait(BaitType.BREAD, "Balta duona"),
                                new Bait(BaitType.CORN, "Saldūs kukurūzai"),
                                new Bait(BaitType.DOUGH, "Tešla"),
                                new Bait(BaitType.FISH_PIECES, "Žuvies gabaliukai"),
                                new Bait(BaitType.SPINNER, "Sukriukė"),
                                new Bait(BaitType.SPOON, "Blizgė"),
                                new Bait(BaitType.SOFT_LURE, "Minkštas guminukas"),
                                new Bait(BaitType.CRANKBAIT, "Vobleris"),
                                new Bait(BaitType.FLY, "Muselė"),
                                new Bait(BaitType.BOILIE, "Boilis"),
                                new Bait(BaitType.POP_UP, "Pop-up boilis"),
                                new Bait(BaitType.PELLET, "Baltyminės granulės"));

                baitRepository.saveAll(baits);

                var location = Location.builder()
                                .latitude(43423.9)
                                .longitude(5454.1)
                                .country("Lithuania")
                                .district("Utena")
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
