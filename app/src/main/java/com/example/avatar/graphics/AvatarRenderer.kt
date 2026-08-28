package com.example.avatar.graphics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.avatar.model.AvatarConfig

import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush

/**
 * Interactive Layered Vector Avatar Canvas View.
 * Supports pan, pinch-to-zoom, resetting view, and strict Z-index layer rendering.
 */
@Composable
fun AvatarRenderer(
    config: AvatarConfig,
    modifier: Modifier = Modifier,
    isZoomable: Boolean = true,
    scaleState: Float = 1f,
    onScaleChanged: (Float) -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(scaleState) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val frostedShape = RoundedCornerShape(28.dp)

    Box(
        modifier = modifier
            .shadow(16.dp, frostedShape, spotColor = Color(0x336750A4))
            .clip(frostedShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xF2F7F2FA),
                        Color(0xE6E8DEF8)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.8f),
                        Color.White.copy(alpha = 0.3f)
                    )
                ),
                shape = frostedShape
            )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(isZoomable) {
                    if (isZoomable) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.7f, 3.0f)
                            onScaleChanged(scale)
                            offset += pan
                        }
                    }
                }
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
        ) {
            val canvasSize = size

            // STRICT Z-INDEX RENDERING HIERARCHY
            // 1. Room Background & Decor
            VectorPaths.drawRoomBackground(this, config.roomId, canvasSize)

            // 2. Companion / Pet
            VectorPaths.drawCompanion(this, config.companionId, canvasSize)

            // 3. Back Hair
            VectorPaths.drawBackHair(this, config, canvasSize)

            // 4. Base Character Body, Skin, Bone Structure, Nails
            VectorPaths.drawBodyAndSkin(this, config, canvasSize)

            // 5. Facial Features (Eyes, Iris, Eyebrows, Eyeliner, Lips, Makeup)
            VectorPaths.drawFacialDetails(this, config, canvasSize)

            // 6. Clothing & Footwear
            VectorPaths.drawClothing(this, config, canvasSize)

            // 7. Front Hair & Bangs
            VectorPaths.drawFrontHair(this, config, canvasSize)

            // 8. Accessories (Tiara, Glasses, Tech)
            VectorPaths.drawAccessories(this, config, canvasSize)

            // 9. Ambient Lighting Overlay
            VectorPaths.drawAmbientLighting(this, config.ambientLighting, canvasSize)
        }
    }
}
