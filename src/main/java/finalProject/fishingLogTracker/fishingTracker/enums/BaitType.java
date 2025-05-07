package finalProject.fishingLogTracker.fishingTracker.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "BaitType")
public enum BaitType {
    WORM,      // Sliekas
    GRUB,      // Dzikas
    SPINNER,   // Blyžgė
    MINNOW,    // Vobleris
    SHRIMP,    // Krevetė
    FLY,       // Musė
    CHEESE,    // Sūris
    MOTH,      // Naktinė musė
    CRAB,      // Krabas
    WAXWORM    // Vaškas
}