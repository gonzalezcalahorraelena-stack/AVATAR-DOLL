package com.example.avatar.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

enum class ArtStyle(val displayName: String, val description: String) {
    ANIME_CHIBI("Anime Chibi", "Ojos grandes expresivos, cabeza tierna y proporciones adorables"),
    BARBIE_KEN("Barbie & Ken", "Estética glamurosa, moda Y2K y proporciones estilizadas"),
    BRATZ("Bratz Style", "Ojos almendrados, labios prominentes y actitud urbana audaz"),
    DISNEY_ROYAL("Princesa Disney", "Cuento de hadas clásico, líneas nobles y elegancia real")
}

enum class Gender(val displayName: String) {
    GIRL("Chica / Femenino"),
    BOY("Chico / Masculino")
}

enum class BodyType(val displayName: String) {
    SLENDER("Delgado"),
    CURVY("Curvilíneo"),
    ATHLETIC("Atlético"),
    PLUS_SIZE("Plus-Size"),
    TALL("Alto"),
    PETITE("Bajito")
}

enum class EyeExpression(val displayName: String) {
    NEUTRAL("Neutral"),
    HAPPY("Sonriente"),
    WINK("Guiño"),
    PLAYFUL("Juguetón"),
    GLAMOROUS("Seductor"),
    FIERCE("Audaz")
}

data class SkinToneOption(val name: String, val hex: String, val isFantasy: Boolean = false)

val SKIN_TONES = listOf(
    SkinToneOption("Porcelana Cálida", "#FFF0E5"),
    SkinToneOption("Arena Suave", "#F7D7C4"),
    SkinToneOption("Dorado Neutro", "#E0AC69"),
    SkinToneOption("Miel Cálida", "#C68642"),
    SkinToneOption("Caramelo Profundo", "#8D5524"),
    SkinToneOption("Ébano Rico", "#4C2B11"),
    // Tonos Fantasía
    SkinToneOption("Azul Cósmico", "#91C8FF", isFantasy = true),
    SkinToneOption("Menta Mágica", "#A8F0D0", isFantasy = true),
    SkinToneOption("Lila Místico", "#E0BBE4", isFantasy = true),
    SkinToneOption("Gris Metálico", "#B0BEC5", isFantasy = true)
)

data class HairStyleOption(val id: String, val name: String, val category: String)

val BANGS_OPTIONS = listOf(
    HairStyleOption("bangs_none", "Sin Flequillo", "Flequillo"),
    HairStyleOption("bangs_straight", "Flequillo Recto", "Flequillo"),
    HairStyleOption("bangs_curtain", "Flequillo Cortina", "Flequillo"),
    HairStyleOption("bangs_side", "Flequillo Ladeado", "Flequillo"),
    HairStyleOption("bangs_wispy", "Flequillo Desfilado", "Flequillo")
)

val SIDES_HAIR_OPTIONS = listOf(
    HairStyleOption("sides_short", "Corto Degradado", "Laterales"),
    HairStyleOption("sides_medium", "Ondas Medias", "Laterales"),
    HairStyleOption("sides_long_straight", "Liso Largo", "Laterales"),
    HairStyleOption("sides_curly", "Rizos Voluminosos", "Laterales"),
    HairStyleOption("sides_braids", "Trenzas Laterales", "Laterales")
)

val BACK_HAIR_OPTIONS = listOf(
    HairStyleOption("back_bob", "Corte Bob", "Trasero"),
    HairStyleOption("back_pigtails", "Coletas Dobles", "Trasero"),
    HairStyleOption("back_high_bun", "Moño Alto", "Trasero"),
    HairStyleOption("back_long_waves", "Ondas Surferas Largas", "Trasero"),
    HairStyleOption("back_afro", "Afro Estilizado", "Trasero"),
    HairStyleOption("back_dreads", "Rastas Urbanas", "Trasero"),
    HairStyleOption("back_royal_gala", "Peinado de Gala", "Trasero")
)

data class EyeOption(val id: String, val name: String)
val EYE_SHAPES = listOf(
    EyeOption("eye_chibi_sparkle", "Expresivo Chibi"),
    EyeOption("eye_almond_glam", "Almendrado Glam"),
    EyeOption("eye_cat_feline", "Felino Audaz"),
    EyeOption("eye_royal_anime", "Cuento de Hadas")
)

data class MakeupOption(
    val id: String,
    val name: String,
    val category: String
)

val EYELINER_STYLES = listOf("Delineado Natural", "Cat-Eye Glam", "Delineado Gráfico", "E-Girl Wing", "Ninguno")
val LIPSTICK_FINISHES = listOf("Mate", "Gloss Brillante", "Satinado", "Metálico")
val NAIL_SHAPES = listOf("Almendra", "Cuadrada", "Stiletto", "Coffin", "Cortas")
val NAIL_DESIGNS = listOf("Monocromática", "Estilo Francés", "Patrón Geométrico", "Glitter Destellos", "Efecto 3D Gemas")

data class ClothingItem(
    val id: String,
    val name: String,
    val category: String, // Top, Bottom, FullOutfit, Shoes
    val defaultColorHex: String
)

val TOPS_LIST = listOf(
    ClothingItem("top_crop", "Crop Top Y2K", "Top", "#FF6B8B"),
    ClothingItem("top_graphic_tee", "Camiseta Gráfica Street", "Top", "#2B2D42"),
    ClothingItem("top_hoodie", "Sudadera Oversize Cozy", "Top", "#8D99AE"),
    ClothingItem("top_blouse", "Blusa Seda de Gala", "Top", "#F7FFF7"),
    ClothingItem("top_corset", "Corset de Encaje", "Top", "#D90429"),
    ClothingItem("top_leather_jacket", "Chaqueta de Cuero", "Top", "#111111")
)

val BOTTOMS_LIST = listOf(
    ClothingItem("bottom_pleated_skirt", "Falda Plisada Escolar", "Bottom", "#3A86FF"),
    ClothingItem("bottom_mom_jeans", "Mom Jeans Vintage", "Bottom", "#4EA8DE"),
    ClothingItem("bottom_cargo", "Pantalón Cargo Urbano", "Bottom", "#52B788"),
    ClothingItem("bottom_pencil_skirt", "Falda Tubo Glam", "Bottom", "#212529"),
    ClothingItem("bottom_shorts", "Shorts Denim", "Bottom", "#90E0EF")
)

val OUTFITS_LIST = listOf(
    ClothingItem("none", "Usar prendas separadas", "FullOutfit", "#000000"),
    ClothingItem("outfit_princess_gown", "Vestido de Gala Real", "FullOutfit", "#FFB703"),
    ClothingItem("outfit_bratz_urban", "Conjunto Cuero & Cadena", "FullOutfit", "#7209B7"),
    ClothingItem("outfit_barbie_pink", "Vestido Pastel Y2K", "FullOutfit", "#FF85A1"),
    ClothingItem("outfit_chibi_kimono", "Kimono Mágico", "FullOutfit", "#FB8500")
)

val SHOES_LIST = listOf(
    ClothingItem("shoes_sneakers", "Zapatillas Urbanas Chunky", "Shoes", "#FFFFFF"),
    ClothingItem("shoes_heels", "Tacones Stiletto Glam", "Shoes", "#D90429"),
    ClothingItem("shoes_combat_boots", "Botas de Combate", "Shoes", "#1A1A1A"),
    ClothingItem("shoes_platforms", "Sandalias Plataforma", "Shoes", "#FF007F")
)

data class AccessoryItem(
    val id: String,
    val name: String,
    val category: String // Jewelry, Head, Eyewear, Tech
)

val ACCESSORIES_LIST = listOf(
    AccessoryItem("acc_tiara", "Tiara de Cristal de Princesa", "Head"),
    AccessoryItem("acc_cap", "Gorra Urbana Streetwear", "Head"),
    AccessoryItem("acc_beanie", "Beanie Acogedor", "Head"),
    AccessoryItem("acc_cat_ears", "Diadema Orejas de Gato RGB", "Head"),
    AccessoryItem("acc_choker", "Gargantilla de Cuero y Corazón", "Jewelry"),
    AccessoryItem("acc_pearl_necklace", "Collar de Perlas Nobles", "Jewelry"),
    AccessoryItem("acc_sunglasses_future", "Gafas Futuristas Y2K", "Eyewear"),
    AccessoryItem("acc_designer_glasses", "Gafas de Diseñador", "Eyewear"),
    AccessoryItem("acc_headphones", "Auriculares Over-Ear Gaming", "Tech"),
    AccessoryItem("acc_phone", "Smartphone con Funda Personalizada", "Tech"),
    AccessoryItem("acc_coffee", "Taza de Café Humeante", "Tech")
)

data class CompanionOption(
    val id: String,
    val name: String,
    val description: String
)

val COMPANION_OPTIONS = listOf(
    CompanionOption("none", "Sin Mascota", "Ningún compañero en pantalla"),
    CompanionOption("pet_dog", "Cachorro Shiba Inu", "Fiel perrito con pañuelo"),
    CompanionOption("pet_cat", "Gatito Místico", "Gato negro con collar dorado"),
    CompanionOption("pet_dragon", "Mini Dragón de Cristal", "Espíritu de fantasía flotante"),
    CompanionOption("pet_phoenix", "Fénix Miniatura", "Aves estelares brillantes")
)

data class RoomOption(
    val id: String,
    val name: String,
    val style: String,
    val mainColorHex: String
)

val ROOM_OPTIONS = listOf(
    RoomOption("room_cozy_bedroom", "Dormitorio Acogedor", "Cozy Aesthetic", "#FFF3E0"),
    RoomOption("room_cyberpunk_neon", "Habitación Cyberpunk", "Neon Sci-Fi", "#12002B"),
    RoomOption("room_princess_castle", "Castillo de Princesa", "Medieval Royal", "#FDF0F6"),
    RoomOption("room_minimalist_studio", "Estudio Minimalista", "Modern Clean", "#ECEFF1"),
    RoomOption("room_gaming_stream", "Setup Gaming & Stream", "RGB Gamer", "#1A1A2E")
)

enum class AmbientLighting(val displayName: String, val overlayColorHex: String) {
    NONE("Luz Natural", "#00000000"),
    SUNSET("Atardecer Dorado", "#33FF9800"),
    NEON_PURPLE("Neón Neomórfico", "#449C27B0"),
    STARRY_GLOW("Brillo Estelar", "#3300BCD4"),
    SOFT_MORNING("Mañana Suave", "#22FFF59D")
}

/**
 * Complete serializable configuration state for an Avatar.
 */
data class AvatarConfig(
    val name: String = "Mi Avatar",
    val artStyle: ArtStyle = ArtStyle.ANIME_CHIBI,
    val gender: Gender = Gender.GIRL,
    val baseModelIndex: Int = 0, // 0..4
    val bodyType: BodyType = BodyType.SLENDER,
    val skinToneHex: String = "#F7D7C4",
    
    // Hair
    val bangsStyleId: String = "bangs_straight",
    val sidesStyleId: String = "sides_long_straight",
    val backStyleId: String = "back_long_waves",
    val hairBaseColorHex: String = "#4A2E2B",
    val hairStreakColorHex: String = "#FF6B8B",
    val hasHairStreaks: Boolean = true,

    // Eyes
    val eyeShapeId: String = "eye_chibi_sparkle",
    val expression: EyeExpression = EyeExpression.HAPPY,
    val eyeColorLeftHex: String = "#3A86FF",
    val eyeColorRightHex: String = "#3A86FF", // Same by default, change for heterochromia
    val hasHeterochromia: Boolean = false,
    val lashThickness: Float = 1.0f,
    val eyebrowColorHex: String = "#331800",

    // Makeup & Face
    val eyeshadowColorHex: String = "#FF85A1",
    val eyelinerStyle: String = "Cat-Eye Glam",
    val blushColorHex: String = "#FF4081",
    val blushIntensity: Float = 0.6f,
    val lipstickColorHex: String = "#D90429",
    val lipstickFinish: String = "Gloss Brillante",
    val hasFreckles: Boolean = true,
    val hasBeautyMark: Boolean = true,
    val faceJewelsHex: String = "#00FFFF",
    val hasFaceJewels: Boolean = false,

    // Nails
    val nailShape: String = "Almendra",
    val nailDesign: String = "Estilo Francés",
    val nailColorHex: String = "#FFB703",

    // Clothes
    val fullOutfitId: String = "none",
    val topClothingId: String = "top_crop",
    val topColorHex: String = "#FF6B8B",
    val bottomClothingId: String = "bottom_pleated_skirt",
    val bottomColorHex: String = "#3A86FF",
    val shoesId: String = "shoes_sneakers",
    val shoesColorHex: String = "#FFFFFF",

    // Accessories
    val headAccessoryId: String = "acc_tiara",
    val jewelryAccessoryId: String = "acc_choker",
    val eyewearAccessoryId: String = "none",
    val techAccessoryId: String = "acc_phone",

    // Pets & Room
    val companionId: String = "pet_cat",
    val roomId: String = "room_cozy_bedroom",
    val ambientLighting: AmbientLighting = AmbientLighting.STARRY_GLOW
)
