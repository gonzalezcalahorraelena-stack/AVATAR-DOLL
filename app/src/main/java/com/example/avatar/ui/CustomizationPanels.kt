package com.example.avatar.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.avatar.model.ACCESSORIES_LIST
import com.example.avatar.model.BANGS_OPTIONS
import com.example.avatar.model.AmbientLighting
import com.example.avatar.model.ArtStyle
import com.example.avatar.model.AvatarConfig
import com.example.avatar.model.BACK_HAIR_OPTIONS
import com.example.avatar.model.BOTTOMS_LIST
import com.example.avatar.model.BodyType
import com.example.avatar.model.COMPANION_OPTIONS
import com.example.avatar.model.EYELINER_STYLES
import com.example.avatar.model.EyeExpression
import com.example.avatar.model.Gender
import com.example.avatar.model.LIPSTICK_FINISHES
import com.example.avatar.model.NAIL_DESIGNS
import com.example.avatar.model.NAIL_SHAPES
import com.example.avatar.model.OUTFITS_LIST
import com.example.avatar.model.ROOM_OPTIONS
import com.example.avatar.model.SHOES_LIST
import com.example.avatar.model.SIDES_HAIR_OPTIONS
import com.example.avatar.model.SKIN_TONES
import com.example.avatar.model.TOPS_LIST

sealed class CustomizationTab(val title: String, val icon: ImageVector) {
    object ArtAndBase : CustomizationTab("Estilos & Base", Icons.Default.Style)
    object Anatomy : CustomizationTab("Anatomía & Piel", Icons.Default.Face)
    object Hair : CustomizationTab("Cabello Modular", Icons.Default.Brush)
    object Eyes : CustomizationTab("Ojos & Expresión", Icons.Default.AutoAwesome)
    object Makeup : CustomizationTab("Maquillaje & Nails", Icons.Default.Palette)
    object Clothing : CustomizationTab("Ropa & Moda", Icons.Default.ShoppingBag)
    object Accessories : CustomizationTab("Accesorios", Icons.Default.AutoAwesome)
    object CompanionsAndRoom : CustomizationTab("Mascotas & Habitación", Icons.Default.Home)
}

val TABS = listOf(
    CustomizationTab.ArtAndBase,
    CustomizationTab.Anatomy,
    CustomizationTab.Hair,
    CustomizationTab.Eyes,
    CustomizationTab.Makeup,
    CustomizationTab.Clothing,
    CustomizationTab.Accessories,
    CustomizationTab.CompanionsAndRoom
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CustomizationDrawerContent(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Tab Header
        PrimaryTabRow(selectedTabIndex = selectedTabIndex) {
            TABS.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    icon = { Icon(tab.icon, contentDescription = tab.title) },
                    text = { Text(tab.title, fontSize = 11.sp, maxLines = 1) },
                    modifier = Modifier.testTag("tab_${tab.title}")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (selectedTabIndex) {
                0 -> ArtAndBasePanel(config, onConfigChange)
                1 -> AnatomyPanel(config, onConfigChange)
                2 -> HairPanel(config, onConfigChange)
                3 -> EyesPanel(config, onConfigChange)
                4 -> MakeupAndNailsPanel(config, onConfigChange)
                5 -> ClothingPanel(config, onConfigChange)
                6 -> AccessoriesPanel(config, onConfigChange)
                7 -> CompanionsAndRoomPanel(config, onConfigChange)
            }
        }
    }
}

// 1. Art Style & Base Presets
@Composable
private fun ArtAndBasePanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Selecciona el Estilo Visual (4 Estéticas Únicas)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    ArtStyle.entries.forEach { style ->
        val isSelected = config.artStyle == style
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onConfigChange { it.copy(artStyle = style) } }
                .border(if (isSelected) 2.dp else 0.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(style.displayName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(style.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Modelo Base & Género", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        FilterChip(
            selected = config.gender == Gender.GIRL,
            onClick = { onConfigChange { it.copy(gender = Gender.GIRL) } },
            label = { Text("Chica / Femenino") },
            leadingIcon = { Icon(Icons.Default.Female, contentDescription = null) }
        )
        FilterChip(
            selected = config.gender == Gender.BOY,
            onClick = { onConfigChange { it.copy(gender = Gender.BOY) } },
            label = { Text("Chico / Masculino") },
            leadingIcon = { Icon(Icons.Default.Male, contentDescription = null) }
        )
    }

    Text("Modelos Base Disponibles (5 Chico + 5 Chica por estilo)", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(5) { idx ->
            AssistChip(
                onClick = { onConfigChange { it.copy(baseModelIndex = idx) } },
                label = { Text("Modelo ${idx + 1}") },
                leadingIcon = { if (config.baseModelIndex == idx) Icon(Icons.Default.Check, contentDescription = null) }
            )
        }
    }
}

// 2. Anatomy & Skin
@Composable
private fun AnatomyPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Tipos de Cuerpo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(BodyType.entries.toTypedArray()) { bType ->
            FilterChip(
                selected = config.bodyType == bType,
                onClick = { onConfigChange { it.copy(bodyType = bType) } },
                label = { Text(bType.displayName) }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Tono de Piel (Naturales & Fantasía)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(SKIN_TONES) { tone ->
            val isSelected = config.skinToneHex.equals(tone.hex, ignoreCase = true)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(android.graphics.Color.parseColor(tone.hex)))
                    .border(if (isSelected) 3.dp else 1.dp, if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray, CircleShape)
                    .clickable { onConfigChange { it.copy(skinToneHex = tone.hex) } }
            )
        }
    }

    AdvancedColorPicker(
        selectedHex = config.skinToneHex,
        onColorSelected = { hex -> onConfigChange { it.copy(skinToneHex = hex) } },
        title = "Ajustar Tono Personalizado"
    )
}

// 3. Hair (Modular Bangs, Sides, Back, Dual Colors)
@Composable
private fun HairPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Flequillo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(BANGS_OPTIONS) { opt ->
            FilterChip(
                selected = config.bangsStyleId == opt.id,
                onClick = { onConfigChange { it.copy(bangsStyleId = opt.id) } },
                label = { Text(opt.name) }
            )
        }
    }

    Text("Cabello Trasero & Estilo General", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(BACK_HAIR_OPTIONS) { opt ->
            FilterChip(
                selected = config.backStyleId == opt.id,
                onClick = { onConfigChange { it.copy(backStyleId = opt.id) } },
                label = { Text(opt.name) }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Color Base del Cabello", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    AdvancedColorPicker(
        selectedHex = config.hairBaseColorHex,
        onColorSelected = { hex -> onConfigChange { it.copy(hairBaseColorHex = hex) } },
        title = "Color Principal"
    )

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("Mechas / Degradado de Color", fontWeight = FontWeight.Bold)
        Switch(checked = config.hasHairStreaks, onCheckedChange = { chk -> onConfigChange { it.copy(hasHairStreaks = chk) } })
    }

    AnimatedVisibility(visible = config.hasHairStreaks) {
        AdvancedColorPicker(
            selectedHex = config.hairStreakColorHex,
            onColorSelected = { hex -> onConfigChange { it.copy(hairStreakColorHex = hex) } },
            title = "Color de Mechas"
        )
    }
}

// 4. Eyes & Expressions
@Composable
private fun EyesPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Expresión Facial", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(EyeExpression.entries.toTypedArray()) { exp ->
            FilterChip(
                selected = config.expression == exp,
                onClick = { onConfigChange { it.copy(expression = exp) } },
                label = { Text(exp.displayName) }
            )
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("Heterocromía (Ojos de Distinto Color)", fontWeight = FontWeight.Bold)
        Switch(checked = config.hasHeterochromia, onCheckedChange = { chk -> onConfigChange { it.copy(hasHeterochromia = chk) } })
    }

    AdvancedColorPicker(
        selectedHex = config.eyeColorLeftHex,
        onColorSelected = { hex -> onConfigChange { it.copy(eyeColorLeftHex = hex) } },
        title = "Color de Iris (Izquierdo)"
    )

    AnimatedVisibility(visible = config.hasHeterochromia) {
        AdvancedColorPicker(
            selectedHex = config.eyeColorRightHex,
            onColorSelected = { hex -> onConfigChange { it.copy(eyeColorRightHex = hex) } },
            title = "Color de Iris (Derecho)"
        )
    }

    Text("Grosor de Pestañas", fontWeight = FontWeight.Bold)
    Slider(
        value = config.lashThickness,
        onValueChange = { valNew -> onConfigChange { it.copy(lashThickness = valNew) } },
        valueRange = 0.5f..2.5f
    )
}

// 5. Makeup & Nails
@Composable
private fun MakeupAndNailsPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Estilo de Delineado (Eyeliner)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(EYELINER_STYLES) { eStyle ->
            FilterChip(
                selected = config.eyelinerStyle == eStyle,
                onClick = { onConfigChange { it.copy(eyelinerStyle = eStyle) } },
                label = { Text(eStyle) }
            )
        }
    }

    AdvancedColorPicker(
        selectedHex = config.lipstickColorHex,
        onColorSelected = { hex -> onConfigChange { it.copy(lipstickColorHex = hex) } },
        title = "Labial & Tono"
    )

    Text("Acabado de Labial", fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(LIPSTICK_FINISHES) { fin ->
            FilterChip(
                selected = config.lipstickFinish == fin,
                onClick = { onConfigChange { it.copy(lipstickFinish = fin) } },
                label = { Text(fin) }
            )
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("Pecas Faciales", fontWeight = FontWeight.Bold)
        Switch(checked = config.hasFreckles, onCheckedChange = { chk -> onConfigChange { it.copy(hasFreckles = chk) } })
    }

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text("Gemas / Pedrería Adhesiva Facial", fontWeight = FontWeight.Bold)
        Switch(checked = config.hasFaceJewels, onCheckedChange = { chk -> onConfigChange { it.copy(hasFaceJewels = chk) } })
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Nail Art (Uñas)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    Text("Forma de Uñas", fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(NAIL_SHAPES) { nShape ->
            FilterChip(
                selected = config.nailShape == nShape,
                onClick = { onConfigChange { it.copy(nailShape = nShape) } },
                label = { Text(nShape) }
            )
        }
    }

    AdvancedColorPicker(
        selectedHex = config.nailColorHex,
        onColorSelected = { hex -> onConfigChange { it.copy(nailColorHex = hex) } },
        title = "Color de Uñas"
    )
}

// 6. Clothing
@Composable
private fun ClothingPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Conjuntos Completos / Vestidos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(OUTFITS_LIST) { outfit ->
            FilterChip(
                selected = config.fullOutfitId == outfit.id,
                onClick = { onConfigChange { it.copy(fullOutfitId = outfit.id) } },
                label = { Text(outfit.name) }
            )
        }
    }

    AnimatedVisibility(visible = config.fullOutfitId == "none") {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Prendas Superiores (Tops & Chaquetas)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(TOPS_LIST) { top ->
                    FilterChip(
                        selected = config.topClothingId == top.id,
                        onClick = { onConfigChange { it.copy(topClothingId = top.id, topColorHex = top.defaultColorHex) } },
                        label = { Text(top.name) }
                    )
                }
            }

            AdvancedColorPicker(
                selectedHex = config.topColorHex,
                onColorSelected = { hex -> onConfigChange { it.copy(topColorHex = hex) } },
                title = "Color de Prenda Superior"
            )

            Text("Prendas Inferiores (Pantalones & Faldas)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(BOTTOMS_LIST) { bot ->
                    FilterChip(
                        selected = config.bottomClothingId == bot.id,
                        onClick = { onConfigChange { it.copy(bottomClothingId = bot.id, bottomColorHex = bot.defaultColorHex) } },
                        label = { Text(bot.name) }
                    )
                }
            }

            AdvancedColorPicker(
                selectedHex = config.bottomColorHex,
                onColorSelected = { hex -> onConfigChange { it.copy(bottomColorHex = hex) } },
                title = "Color de Prenda Inferior"
            )
        }
    }

    Text("Calzado (Zapatillas, Tacones, Botas)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(SHOES_LIST) { shoe ->
            FilterChip(
                selected = config.shoesId == shoe.id,
                onClick = { onConfigChange { it.copy(shoesId = shoe.id, shoesColorHex = shoe.defaultColorHex) } },
                label = { Text(shoe.name) }
            )
        }
    }
}

// 7. Accessories
@Composable
private fun AccessoriesPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Joyas, Sombreros, Gafas & Tecnología", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    ACCESSORIES_LIST.forEach { acc ->
        val isSelected = when (acc.category) {
            "Head" -> config.headAccessoryId == acc.id
            "Jewelry" -> config.jewelryAccessoryId == acc.id
            "Eyewear" -> config.eyewearAccessoryId == acc.id
            "Tech" -> config.techAccessoryId == acc.id
            else -> false
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onConfigChange { current ->
                        when (acc.category) {
                            "Head" -> current.copy(headAccessoryId = if (isSelected) "none" else acc.id)
                            "Jewelry" -> current.copy(jewelryAccessoryId = if (isSelected) "none" else acc.id)
                            "Eyewear" -> current.copy(eyewearAccessoryId = if (isSelected) "none" else acc.id)
                            "Tech" -> current.copy(techAccessoryId = if (isSelected) "none" else acc.id)
                            else -> current
                        }
                    }
                }
                .border(if (isSelected) 2.dp else 0.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(acc.name, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (isSelected) Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

// 8. Companions & Room
@Composable
private fun CompanionsAndRoomPanel(
    config: AvatarConfig,
    onConfigChange: ((AvatarConfig) -> AvatarConfig) -> Unit
) {
    Text("Mascota / Compañero Místico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(COMPANION_OPTIONS) { pet ->
            FilterChip(
                selected = config.companionId == pet.id,
                onClick = { onConfigChange { it.copy(companionId = pet.id) } },
                label = { Text(pet.name) }
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Habitaciones Temáticas & Decoración", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

    ROOM_OPTIONS.forEach { rm ->
        val isSelected = config.roomId == rm.id
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onConfigChange { it.copy(roomId = rm.id) } }
                .border(if (isSelected) 2.dp else 0.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(rm.name, fontWeight = FontWeight.Bold)
                Text("Estilo: ${rm.style}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text("Iluminación Ambiental & Filtros", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(AmbientLighting.entries.toTypedArray()) { light ->
            FilterChip(
                selected = config.ambientLighting == light,
                onClick = { onConfigChange { it.copy(ambientLighting = light) } },
                label = { Text(light.displayName) }
            )
        }
    }
}
