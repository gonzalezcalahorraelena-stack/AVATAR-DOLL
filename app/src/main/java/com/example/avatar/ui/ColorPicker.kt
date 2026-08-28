package com.example.avatar.ui

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val POPULAR_SWATCHES = listOf(
    "#FFF0E5", "#F7D7C4", "#E0AC69", "#8D5524", "#4C2B11", // Skins
    "#FF6B8B", "#3A86FF", "#52B788", "#FFB703", "#7209B7", // Vibrant
    "#91C8FF", "#A8F0D0", "#E0BBE4", "#FF007F", "#111111"  // Fantasy/Dark
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdvancedColorPicker(
    selectedHex: String,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Color Customization"
) {
    var colorInt by remember(selectedHex) {
        val cleanHex = selectedHex.removePrefix("#")
        val parsed = try {
            if (cleanHex.length == 6) "FF$cleanHex".toLong(16).toInt() else cleanHex.toLong(16).toInt()
        } catch (e: Exception) {
            Color.Red.toArgb()
        }
        mutableFloatStateOf(parsed.toFloat())
    }

    var redVal by remember(selectedHex) { mutableFloatStateOf(Color(colorInt.toInt()).red * 255f) }
    var greenVal by remember(selectedHex) { mutableFloatStateOf(Color(colorInt.toInt()).green * 255f) }
    var blueVal by remember(selectedHex) { mutableFloatStateOf(Color(colorInt.toInt()).blue * 255f) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(selectedHex)))
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Swatches
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                POPULAR_SWATCHES.forEach { hex ->
                    val isSelected = hex.equals(selectedHex, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(hex)))
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(hex) }
                            .testTag("color_swatch_$hex")
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // RGB Sliders for Custom Tuning
            Column {
                RGBRow("Rojo", redVal, Color.Red) { newR ->
                    redVal = newR
                    val newHex = String.format("#%02X%02X%02X", redVal.toInt(), greenVal.toInt(), blueVal.toInt())
                    onColorSelected(newHex)
                }
                RGBRow("Verde", greenVal, Color.Green) { newG ->
                    greenVal = newG
                    val newHex = String.format("#%02X%02X%02X", redVal.toInt(), greenVal.toInt(), blueVal.toInt())
                    onColorSelected(newHex)
                }
                RGBRow("Azul", blueVal, Color.Blue) { newB ->
                    blueVal = newB
                    val newHex = String.format("#%02X%02X%02X", redVal.toInt(), greenVal.toInt(), blueVal.toInt())
                    onColorSelected(newHex)
                }
            }
        }
    }
}

@Composable
private fun RGBRow(
    label: String,
    value: Float,
    sliderColor: Color,
    onValueChange: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, fontSize = 11.sp, modifier = Modifier.width(42.dp))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..255f,
            modifier = Modifier.weight(1f)
        )
        Text(text = value.toInt().toString(), fontSize = 11.sp, modifier = Modifier.width(30.dp))
    }
}
