package finalProject.fishingLogTracker.fishingTracker.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "BaitType")
public enum BaitType {

    EARTHWORM, // Sliekas
    NIGHTCRAWLER, // Naktinis sliekas
    BLOODWORM, // Uodo trūklio lerva
    MAGGOT, // Musės lerva
    LEECH, // Dėlės
    MINNOW, // Mažos žuvelės
    BREAD, // Duona
    CORN, // Kukurūzai
    DOUGH, // Tešla
    FISH_PIECES, // Žuvies gabalai
    SPINNER, // Sukriukė
    SPOON, // Blizgė
    SOFT_LURE, // Minkštas masalas
    CRANKBAIT, // Plūduriuojantis masalas
    FLY, // Muselė
    BOILIE, // Boilis
    POP_UP, // Pop-up boilis
    PELLET, // Granulės
    OTHER // Kitas
}