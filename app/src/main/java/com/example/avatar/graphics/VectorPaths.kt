package com.example.avatar.graphics

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.avatar.model.AmbientLighting
import com.example.avatar.model.ArtStyle
import com.example.avatar.model.AvatarConfig
import com.example.avatar.model.BodyType
import com.example.avatar.model.EyeExpression
import com.example.avatar.model.Gender
import kotlin.math.sin

/**
 * Utility functions to parse Hex color string safely into Compose Color.
 */
fun parseHexColor(hex: String, defaultColor: Color = Color.LightGray): Color {
    return try {
        val cleanHex = hex.removePrefix("#")
        val colorInt = when (cleanHex.length) {
            6 -> "FF$cleanHex".toLong(16)
            8 -> cleanHex.toLong(16)
            else -> return defaultColor
        }
        Color(colorInt)
    } catch (e: Exception) {
        defaultColor
    }
}

/**
 * High-performance vector drawing methods for 2D layered avatar graphics.
 */
object VectorPaths {

    // Helper: Draw Room Background & Furniture
    fun drawRoomBackground(scope: DrawScope, roomId: String, canvasSize: Size) {
        val width = canvasSize.width
        val height = canvasSize.height

        when (roomId) {
            "room_cozy_bedroom" -> {
                // Wall gradient
                scope.drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
                    ),
                    size = canvasSize
                )
                // Floor
                scope.drawRect(
                    color = Color(0xFFD7CCC8),
                    topLeft = Offset(0f, height * 0.7f),
                    size = Size(width, height * 0.3f)
                )
                // Cozy Rug
                scope.drawOval(
                    color = Color(0xFFFFCC80),
                    topLeft = Offset(width * 0.15f, height * 0.65f),
                    size = Size(width * 0.7f, height * 0.25f)
                )
                // Fairy lights string on wall
                val lightString = Path().apply {
                    moveTo(0f, height * 0.15f)
                    quadraticTo(width * 0.5f, height * 0.25f, width, height * 0.12f)
                }
                scope.drawPath(lightString, color = Color(0xFF8D6E63), style = Stroke(2f))
                val lightColors = listOf(Color(0xFFFFD54F), Color(0xFFFF8A80), Color(0xFF80D8FF), Color(0xFFCCFF90))
                for (i in 0..10) {
                    val t = i / 10f
                    val lx = width * t
                    val ly = height * (0.15f + 0.1f * sin(t * Math.PI.toFloat()))
                    scope.drawCircle(lightColors[i % lightColors.size], radius = 8f, center = Offset(lx, ly))
                }
            }

            "room_cyberpunk_neon" -> {
                // Cyberpunk Wall
                scope.drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F051D), Color(0xFF290A38))
                    ),
                    size = canvasSize
                )
                // Neon Grid Floor
                scope.drawRect(
                    color = Color(0xFF0A0012),
                    topLeft = Offset(0f, height * 0.7f),
                    size = Size(width, height * 0.3f)
                )
                // Neon grid lines
                for (i in 0..8) {
                    val x = width * (i / 8f)
                    scope.drawLine(
                        color = Color(0xFF00E5FF),
                        start = Offset(width * 0.5f, height * 0.7f),
                        end = Offset(x, height),
                        strokeWidth = 2f
                    )
                }
                // Neon Ring Sign
                scope.drawCircle(
                    brush = Brush.radialGradient(listOf(Color(0xFFFF007F), Color.Transparent)),
                    radius = width * 0.35f,
                    center = Offset(width * 0.5f, height * 0.35f)
                )
            }

            "room_princess_castle" -> {
                // Castle Wall
                scope.drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF8E1EE), Color(0xFFE1BEE7))
                    ),
                    size = canvasSize
                )
                // Arch window
                val arch = Path().apply {
                    moveTo(width * 0.3f, height * 0.4f)
                    lineTo(width * 0.3f, height * 0.2f)
                    quadraticTo(width * 0.5f, height * 0.05f, width * 0.7f, height * 0.2f)
                    lineTo(width * 0.7f, height * 0.4f)
                    close()
                }
                scope.drawPath(arch, color = Color(0xFFB39DDB))
                scope.drawPath(arch, color = Color(0xFFD1C4E9), style = Stroke(4f))
                // Carpet
                scope.drawOval(
                    color = Color(0xFFAB47BC),
                    topLeft = Offset(width * 0.1f, height * 0.72f),
                    size = Size(width * 0.8f, height * 0.2f)
                )
            }

            "room_minimalist_studio" -> {
                // Minimalist Wall
                scope.drawRect(color = Color(0xFFECEFF1), size = canvasSize)
                // Hardwood Floor
                scope.drawRect(
                    color = Color(0xFFCFD8DC),
                    topLeft = Offset(0f, height * 0.72f),
                    size = Size(width, height * 0.28f)
                )
                // Modern Plant Pot
                scope.drawRect(
                    color = Color(0xFF78909C),
                    topLeft = Offset(width * 0.1f, height * 0.6f),
                    size = Size(width * 0.12f, height * 0.18f)
                )
                // Plant Leaves
                scope.drawCircle(Color(0xFF2E7D32), radius = width * 0.09f, center = Offset(width * 0.16f, height * 0.55f))
            }

            "room_gaming_stream" -> {
                // Dark Gaming Wall
                scope.drawRect(color = Color(0xFF1A1A2E), size = canvasSize)
                // RGB Wall Bars
                scope.drawRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFF0055), Color(0xFF7A00FF))),
                    topLeft = Offset(width * 0.08f, height * 0.1f),
                    size = Size(width * 0.04f, height * 0.5f)
                )
                scope.drawRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFF00E5FF), Color(0xFF00FF66))),
                    topLeft = Offset(width * 0.88f, height * 0.1f),
                    size = Size(width * 0.04f, height * 0.5f)
                )
                // Gaming Chair shadow
                scope.drawCircle(Color(0xFF0F3460), radius = width * 0.35f, center = Offset(width * 0.5f, height * 0.6f))
            }
        }
    }

    // Companion / Pet
    fun drawCompanion(scope: DrawScope, companionId: String, canvasSize: Size) {
        if (companionId == "none") return
        val w = canvasSize.width
        val h = canvasSize.height
        val cx = w * 0.8f
        val cy = h * 0.68f

        when (companionId) {
            "pet_dog" -> {
                // Shiba Inu
                scope.drawCircle(Color(0xFFE67E22), radius = 35f, center = Offset(cx, cy)) // Body
                scope.drawCircle(Color(0xFFF39C12), radius = 25f, center = Offset(cx - 10f, cy - 25f)) // Head
                scope.drawCircle(Color(0xFFFFFFFF), radius = 10f, center = Offset(cx - 16f, cy - 20f)) // Muzzle
                scope.drawCircle(Color(0xFF000000), radius = 3f, center = Offset(cx - 18f, cy - 22f)) // Nose
                scope.drawCircle(Color(0xFF000000), radius = 3f, center = Offset(cx - 10f, cy - 28f)) // Eye
                // Scarf
                scope.drawCircle(Color(0xFFE74C3C), radius = 12f, center = Offset(cx - 8f, cy - 8f))
            }

            "pet_cat" -> {
                // Black Mystic Cat
                scope.drawCircle(Color(0xFF2C3E50), radius = 30f, center = Offset(cx, cy))
                scope.drawCircle(Color(0xFF1A252F), radius = 22f, center = Offset(cx - 5f, cy - 22f))
                // Glowing eyes
                scope.drawCircle(Color(0xFFF1C40F), radius = 4f, center = Offset(cx - 12f, cy - 24f))
                scope.drawCircle(Color(0xFFF1C40F), radius = 4f, center = Offset(cx + 2f, cy - 24f))
                // Cat ears
                val ear1 = Path().apply {
                    moveTo(cx - 18f, cy - 35f)
                    lineTo(cx - 10f, cy - 45f)
                    lineTo(cx - 5f, cy - 32f)
                }
                scope.drawPath(ear1, color = Color(0xFF1A252F))
            }

            "pet_dragon" -> {
                // Mini Dragon
                scope.drawCircle(Color(0xFF9B59B6), radius = 28f, center = Offset(cx, cy - 20f)) // Floating
                scope.drawCircle(Color(0xFF8E44AD), radius = 22f, center = Offset(cx - 12f, cy - 40f))
                // Dragon wings
                val wing = Path().apply {
                    moveTo(cx, cy - 20f)
                    lineTo(cx + 30f, cy - 45f)
                    lineTo(cx + 20f, cy - 10f)
                }
                scope.drawPath(wing, color = Color(0xFFE1BEE7))
                // Star glow
                scope.drawCircle(Color(0xFFF39C12), radius = 6f, center = Offset(cx - 18f, cy - 42f))
            }

            "pet_phoenix" -> {
                // Mini Phoenix
                scope.drawCircle(
                    brush = Brush.radialGradient(listOf(Color(0xFFFF5722), Color(0xFFFFC107))),
                    radius = 26f,
                    center = Offset(cx, cy - 30f)
                )
                // Sparkles around
                scope.drawCircle(Color(0xFFFFEB3B), radius = 4f, center = Offset(cx + 20f, cy - 45f))
                scope.drawCircle(Color(0xFFFF9800), radius = 3f, center = Offset(cx - 20f, cy - 15f))
            }
        }
    }

    // Base Character Body & Skin
    fun drawBodyAndSkin(
        scope: DrawScope,
        config: AvatarConfig,
        canvasSize: Size
    ) {
        val w = canvasSize.width
        val h = canvasSize.height
        val skinColor = parseHexColor(config.skinToneHex)
        val shadowColor = skinColor.copy(
            red = (skinColor.red * 0.8f).coerceAtLeast(0f),
            green = (skinColor.green * 0.8f).coerceAtLeast(0f),
            blue = (skinColor.blue * 0.8f).coerceAtLeast(0f)
        )

        val headScale = when (config.artStyle) {
            ArtStyle.ANIME_CHIBI -> 1.35f
            ArtStyle.BARBIE_KEN -> 0.95f
            ArtStyle.BRATZ -> 1.15f
            ArtStyle.DISNEY_ROYAL -> 1.0f
        }

        val baseLineWeight = when (config.artStyle) {
            ArtStyle.ANIME_CHIBI -> 2.0f
            ArtStyle.BARBIE_KEN -> 2.6f
            ArtStyle.BRATZ -> 3.8f
            ArtStyle.DISNEY_ROYAL -> 2.2f
        }

        val bodyWidthFactor = when (config.bodyType) {
            BodyType.SLENDER -> 0.85f
            BodyType.CURVY -> 1.15f
            BodyType.ATHLETIC -> 1.05f
            BodyType.PLUS_SIZE -> 1.3f
            BodyType.TALL -> 0.9f
            BodyType.PETITE -> 0.95f
        }

        // 1. Torso & Shoulders
        val shoulderWidth = w * 0.32f * bodyWidthFactor
        val chestTop = h * 0.45f
        val chestBottom = h * 0.75f

        val torsoPath = Path().apply {
            moveTo(w * 0.5f - shoulderWidth * 0.5f, chestTop)
            lineTo(w * 0.5f + shoulderWidth * 0.5f, chestTop)
            lineTo(w * 0.5f + shoulderWidth * 0.4f, chestBottom)
            lineTo(w * 0.5f - shoulderWidth * 0.4f, chestBottom)
            close()
        }
        scope.drawPath(torsoPath, color = skinColor)
        scope.drawPath(torsoPath, color = shadowColor, style = Stroke(baseLineWeight))

        // Neck
        val neckWidth = w * 0.08f * (if (config.gender == Gender.BOY) 1.2f else 0.9f)
        val neckPath = Path().apply {
            moveTo(w * 0.5f - neckWidth, chestTop - h * 0.08f)
            lineTo(w * 0.5f + neckWidth, chestTop - h * 0.08f)
            lineTo(w * 0.5f + neckWidth * 1.1f, chestTop)
            lineTo(w * 0.5f - neckWidth * 1.1f, chestTop)
            close()
        }
        scope.drawPath(neckPath, color = skinColor)
        scope.drawPath(neckPath, color = shadowColor, style = Stroke(baseLineWeight))

        // Style Shading Dynamics: Neck & Chest
        when (config.artStyle) {
            ArtStyle.DISNEY_ROYAL -> {
                // Fairytale soft neck gradient shadow
                scope.drawPath(
                    Path().apply {
                        moveTo(w * 0.5f - neckWidth, chestTop - h * 0.08f)
                        lineTo(w * 0.5f + neckWidth, chestTop - h * 0.08f)
                        lineTo(w * 0.5f + neckWidth * 0.9f, chestTop - h * 0.02f)
                        lineTo(w * 0.5f - neckWidth * 0.9f, chestTop - h * 0.02f)
                        close()
                    },
                    color = shadowColor.copy(alpha = 0.4f)
                )
            }
            ArtStyle.BRATZ -> {
                // Bold dramatic neck contour
                scope.drawPath(
                    Path().apply {
                        moveTo(w * 0.5f - neckWidth, chestTop - h * 0.08f)
                        lineTo(w * 0.5f + neckWidth, chestTop - h * 0.08f)
                        lineTo(w * 0.5f + neckWidth * 1.1f, chestTop - h * 0.04f)
                        lineTo(w * 0.5f - neckWidth * 1.1f, chestTop - h * 0.04f)
                        close()
                    },
                    color = shadowColor
                )
            }
            ArtStyle.BARBIE_KEN -> {
                // Sun-kissed collarbone highlight
                scope.drawLine(
                    color = Color.White.copy(alpha = 0.6f),
                    start = Offset(w * 0.5f - shoulderWidth * 0.25f, chestTop + h * 0.02f),
                    end = Offset(w * 0.5f - shoulderWidth * 0.05f, chestTop + h * 0.035f),
                    strokeWidth = 2.5f
                )
                scope.drawLine(
                    color = Color.White.copy(alpha = 0.6f),
                    start = Offset(w * 0.5f + shoulderWidth * 0.05f, chestTop + h * 0.035f),
                    end = Offset(w * 0.5f + shoulderWidth * 0.25f, chestTop + h * 0.02f),
                    strokeWidth = 2.5f
                )
            }
            ArtStyle.ANIME_CHIBI -> {
                // Simple soft cell-shading band
                scope.drawRect(
                    color = shadowColor.copy(alpha = 0.5f),
                    topLeft = Offset(w * 0.5f - neckWidth * 0.9f, chestTop - h * 0.08f),
                    size = Size(neckWidth * 1.8f, h * 0.025f)
                )
            }
        }

        // Arms
        val armWidth = w * 0.05f * bodyWidthFactor
        // Left Arm
        scope.drawRoundRect(
            color = skinColor,
            topLeft = Offset(w * 0.5f - shoulderWidth * 0.52f - armWidth, chestTop),
            size = Size(armWidth, h * 0.28f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Right Arm
        scope.drawRoundRect(
            color = skinColor,
            topLeft = Offset(w * 0.5f + shoulderWidth * 0.52f, chestTop),
            size = Size(armWidth, h * 0.28f),
            cornerRadius = CornerRadius(10f, 10f)
        )

        // Nail Art on hands
        val nailColor = parseHexColor(config.nailColorHex)
        scope.drawCircle(nailColor, radius = 3f, center = Offset(w * 0.5f - shoulderWidth * 0.52f - armWidth * 0.5f, chestTop + h * 0.27f))
        scope.drawCircle(nailColor, radius = 3f, center = Offset(w * 0.5f + shoulderWidth * 0.52f + armWidth * 0.5f, chestTop + h * 0.27f))

        // 2. Head Shape according to ArtStyle & BaseModel Index
        val headCenter = Offset(w * 0.5f, h * 0.28f)
        val headRadiusX = w * 0.22f * headScale
        val headRadiusY = h * 0.18f * headScale

        val headPath = Path().apply {
            when (config.artStyle) {
                ArtStyle.ANIME_CHIBI -> {
                    // Round fluffy cheeks & cute chin
                    moveTo(headCenter.x - headRadiusX, headCenter.y - headRadiusY * 0.5f)
                    cubicTo(
                        headCenter.x - headRadiusX, headCenter.y - headRadiusY * 1.2f,
                        headCenter.x + headRadiusX, headCenter.y - headRadiusY * 1.2f,
                        headCenter.x + headRadiusX, headCenter.y - headRadiusY * 0.5f
                    )
                    cubicTo(
                        headCenter.x + headRadiusX * 1.2f, headCenter.y + headRadiusY * 0.5f,
                        headCenter.x + headRadiusX * 0.4f, headCenter.y + headRadiusY * 1.1f,
                        headCenter.x, headCenter.y + headRadiusY * 1.15f
                    )
                    cubicTo(
                        headCenter.x - headRadiusX * 0.4f, headCenter.y + headRadiusY * 1.1f,
                        headCenter.x - headRadiusX * 1.2f, headCenter.y + headRadiusY * 0.5f,
                        headCenter.x - headRadiusX, headCenter.y - headRadiusY * 0.5f
                    )
                }

                ArtStyle.BARBIE_KEN -> {
                    // Sculpted high cheekbones & elegant jaw
                    moveTo(headCenter.x - headRadiusX * 0.9f, headCenter.y - headRadiusY)
                    lineTo(headCenter.x + headRadiusX * 0.9f, headCenter.y - headRadiusY)
                    lineTo(headCenter.x + headRadiusX, headCenter.y + headRadiusY * 0.2f)
                    lineTo(headCenter.x + headRadiusX * 0.4f, headCenter.y + headRadiusY)
                    lineTo(headCenter.x - headRadiusX * 0.4f, headCenter.y + headRadiusY)
                    lineTo(headCenter.x - headRadiusX, headCenter.y + headRadiusY * 0.2f)
                    close()
                }

                ArtStyle.BRATZ -> {
                    // Distinct jawline & plump lower face outline
                    moveTo(headCenter.x - headRadiusX, headCenter.y - headRadiusY * 0.8f)
                    quadraticTo(headCenter.x, headCenter.y - headRadiusY * 1.3f, headCenter.x + headRadiusX, headCenter.y - headRadiusY * 0.8f)
                    quadraticTo(headCenter.x + headRadiusX * 1.1f, headCenter.y + headRadiusY * 0.3f, headCenter.x + headRadiusX * 0.5f, headCenter.y + headRadiusY * 0.95f)
                    lineTo(headCenter.x - headRadiusX * 0.5f, headCenter.y + headRadiusY * 0.95f)
                    quadraticTo(headCenter.x - headRadiusX * 1.1f, headCenter.y + headRadiusY * 0.3f, headCenter.x - headRadiusX, headCenter.y - headRadiusY * 0.8f)
                }

                ArtStyle.DISNEY_ROYAL -> {
                    // Soft, noble oval face
                    addOval(androidx.compose.ui.geometry.Rect(
                        headCenter.x - headRadiusX,
                        headCenter.y - headRadiusY,
                        headCenter.x + headRadiusX,
                        headCenter.y + headRadiusY
                    ))
                }
            }
        }

        // Draw Head Fill & Outline
        scope.drawPath(headPath, color = skinColor)
        scope.drawPath(headPath, color = shadowColor, style = Stroke(baseLineWeight))

        // Extra Style Highlights & Shading on Head Canvas
        when (config.artStyle) {
            ArtStyle.ANIME_CHIBI -> {
                // Soft cheek highlight sparkles
                val cheekSparkleColor = Color.White.copy(alpha = 0.7f)
                scope.drawCircle(cheekSparkleColor, radius = 3.5f, center = Offset(headCenter.x - headRadiusX * 0.5f, headCenter.y + headRadiusY * 0.4f))
                scope.drawCircle(cheekSparkleColor, radius = 3.5f, center = Offset(headCenter.x + headRadiusX * 0.5f, headCenter.y + headRadiusY * 0.4f))
            }
            ArtStyle.BARBIE_KEN -> {
                // Sculpted cheekbone contour shadow
                val contourColor = shadowColor.copy(alpha = 0.35f)
                scope.drawLine(contourColor, start = Offset(headCenter.x - headRadiusX * 0.85f, headCenter.y + headRadiusY * 0.1f), end = Offset(headCenter.x - headRadiusX * 0.45f, headCenter.y + headRadiusY * 0.4f), strokeWidth = 3f)
                scope.drawLine(contourColor, start = Offset(headCenter.x + headRadiusX * 0.85f, headCenter.y + headRadiusY * 0.1f), end = Offset(headCenter.x + headRadiusX * 0.45f, headCenter.y + headRadiusY * 0.4f), strokeWidth = 3f)
            }
            ArtStyle.BRATZ -> {
                // High contrast chin shadow curve
                val chinShadowPath = Path().apply {
                    moveTo(headCenter.x - headRadiusX * 0.4f, headCenter.y + headRadiusY * 0.85f)
                    quadraticTo(headCenter.x, headCenter.y + headRadiusY * 1.05f, headCenter.x + headRadiusX * 0.4f, headCenter.y + headRadiusY * 0.85f)
                }
                scope.drawPath(chinShadowPath, color = shadowColor.copy(alpha = 0.6f), style = Stroke(4f))
            }
            ArtStyle.DISNEY_ROYAL -> {
                // Fairytale soft forehead highlight glow
                scope.drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                        center = Offset(headCenter.x, headCenter.y - headRadiusY * 0.4f),
                        radius = headRadiusX * 0.6f
                    ),
                    radius = headRadiusX * 0.6f,
                    center = Offset(headCenter.x, headCenter.y - headRadiusY * 0.4f)
                )
            }
        }

        // Ears
        val earLeft = Offset(headCenter.x - headRadiusX * 0.95f, headCenter.y)
        val earRight = Offset(headCenter.x + headRadiusX * 0.95f, headCenter.y)
        scope.drawCircle(skinColor, radius = headRadiusY * 0.22f, center = earLeft)
        scope.drawCircle(skinColor, radius = headRadiusY * 0.22f, center = earRight)
        scope.drawCircle(shadowColor, radius = headRadiusY * 0.22f, center = earLeft, style = Stroke(1.5f))
        scope.drawCircle(shadowColor, radius = headRadiusY * 0.22f, center = earRight, style = Stroke(1.5f))
    }

    // Back Hair
    fun drawBackHair(scope: DrawScope, config: AvatarConfig, canvasSize: Size) {
        val w = canvasSize.width
        val h = canvasSize.height
        val hairColor = parseHexColor(config.hairBaseColorHex)
        val streakColor = parseHexColor(config.hairStreakColorHex)

        val headCenter = Offset(w * 0.5f, h * 0.28f)
        val hairWidth = w * 0.38f

        when (config.backStyleId) {
            "back_long_waves", "back_royal_gala" -> {
                val backHairPath = Path().apply {
                    moveTo(headCenter.x - hairWidth, headCenter.y - h * 0.12f)
                    lineTo(headCenter.x + hairWidth, headCenter.y - h * 0.12f)
                    quadraticTo(headCenter.x + hairWidth * 1.2f, headCenter.y + h * 0.3f, headCenter.x + hairWidth * 0.8f, headCenter.y + h * 0.45f)
                    lineTo(headCenter.x - hairWidth * 0.8f, headCenter.y + h * 0.45f)
                    quadraticTo(headCenter.x - hairWidth * 1.2f, headCenter.y + h * 0.3f, headCenter.x - hairWidth, headCenter.y - h * 0.12f)
                }
                scope.drawPath(backHairPath, color = hairColor)
                if (config.hasHairStreaks) {
                    scope.drawPath(backHairPath, color = streakColor, style = Stroke(8f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 30f))))
                }
            }

            "back_pigtails" -> {
                // Left Pigtail
                scope.drawOval(
                    color = hairColor,
                    topLeft = Offset(headCenter.x - hairWidth * 1.3f, headCenter.y + h * 0.05f),
                    size = Size(hairWidth * 0.6f, h * 0.3f)
                )
                // Right Pigtail
                scope.drawOval(
                    color = hairColor,
                    topLeft = Offset(headCenter.x + hairWidth * 0.7f, headCenter.y + h * 0.05f),
                    size = Size(hairWidth * 0.6f, h * 0.3f)
                )
            }

            "back_high_bun" -> {
                scope.drawCircle(hairColor, radius = hairWidth * 0.45f, center = Offset(headCenter.x, headCenter.y - h * 0.22f))
                if (config.hasHairStreaks) {
                    scope.drawCircle(streakColor, radius = hairWidth * 0.2f, center = Offset(headCenter.x, headCenter.y - h * 0.22f))
                }
            }

            "back_afro", "back_dreads" -> {
                scope.drawCircle(hairColor, radius = hairWidth * 0.7f, center = Offset(headCenter.x, headCenter.y - h * 0.05f))
            }
        }
    }

    // Facial Features: Eyes, Brows, Nose, Mouth, Makeup
    fun drawFacialDetails(scope: DrawScope, config: AvatarConfig, canvasSize: Size) {
        val w = canvasSize.width
        val h = canvasSize.height
        val headCenter = Offset(w * 0.5f, h * 0.28f)

        val eyeSpacing = w * 0.12f
        val eyeY = headCenter.y + h * 0.01f
        val eyeSize = when (config.artStyle) {
            ArtStyle.ANIME_CHIBI -> w * 0.08f
            ArtStyle.BRATZ -> w * 0.075f
            ArtStyle.BARBIE_KEN -> w * 0.055f
            ArtStyle.DISNEY_ROYAL -> w * 0.065f
        }

        val leftEyeCenter = Offset(headCenter.x - eyeSpacing, eyeY)
        val rightEyeCenter = Offset(headCenter.x + eyeSpacing, eyeY)

        val leftIrisColor = parseHexColor(config.eyeColorLeftHex)
        val rightIrisColor = if (config.hasHeterochromia) parseHexColor(config.eyeColorRightHex) else leftIrisColor

        // 1. Eyeshadow Makeup
        val eyeshadowColor = parseHexColor(config.eyeshadowColorHex).copy(alpha = 0.5f)
        scope.drawOval(
            color = eyeshadowColor,
            topLeft = Offset(leftEyeCenter.x - eyeSize * 1.2f, leftEyeCenter.y - eyeSize * 1.1f),
            size = Size(eyeSize * 2.4f, eyeSize * 1.5f)
        )
        scope.drawOval(
            color = eyeshadowColor,
            topLeft = Offset(rightEyeCenter.x - eyeSize * 1.2f, rightEyeCenter.y - eyeSize * 1.1f),
            size = Size(eyeSize * 2.4f, eyeSize * 1.5f)
        )

        // 2. Eye Sclera (White)
        scope.drawOval(Color.White, topLeft = Offset(leftEyeCenter.x - eyeSize, leftEyeCenter.y - eyeSize * 0.8f), size = Size(eyeSize * 2f, eyeSize * 1.6f))
        scope.drawOval(Color.White, topLeft = Offset(rightEyeCenter.x - eyeSize, rightEyeCenter.y - eyeSize * 0.8f), size = Size(eyeSize * 2f, eyeSize * 1.6f))

        // 3. Iris
        val irisRadius = eyeSize * 0.65f
        scope.drawCircle(leftIrisColor, radius = irisRadius, center = leftEyeCenter)
        scope.drawCircle(rightIrisColor, radius = irisRadius, center = rightEyeCenter)

        // Pupil
        scope.drawCircle(Color.Black, radius = irisRadius * 0.5f, center = leftEyeCenter)
        scope.drawCircle(Color.Black, radius = irisRadius * 0.5f, center = rightEyeCenter)

        // Eye Glint / Sparkle
        scope.drawCircle(Color.White, radius = irisRadius * 0.28f, center = Offset(leftEyeCenter.x - irisRadius * 0.3f, leftEyeCenter.y - irisRadius * 0.3f))
        scope.drawCircle(Color.White, radius = irisRadius * 0.28f, center = Offset(rightEyeCenter.x - irisRadius * 0.3f, rightEyeCenter.y - irisRadius * 0.3f))

        // 4. Eyeliner & Lashes
        val lashColor = Color(0xFF111111)
        val lashWidth = 3f * config.lashThickness
        scope.drawArc(
            color = lashColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(leftEyeCenter.x - eyeSize, leftEyeCenter.y - eyeSize * 0.85f),
            size = Size(eyeSize * 2f, eyeSize * 1.6f),
            style = Stroke(lashWidth)
        )
        scope.drawArc(
            color = lashColor,
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(rightEyeCenter.x - eyeSize, rightEyeCenter.y - eyeSize * 0.85f),
            size = Size(eyeSize * 2f, eyeSize * 1.6f),
            style = Stroke(lashWidth)
        )

        // Cat-Eye Flick
        if (config.eyelinerStyle == "Cat-Eye Glam" || config.artStyle == ArtStyle.BRATZ) {
            scope.drawLine(lashColor, start = Offset(leftEyeCenter.x + eyeSize, leftEyeCenter.y - eyeSize * 0.2f), end = Offset(leftEyeCenter.x + eyeSize * 1.4f, leftEyeCenter.y - eyeSize * 0.6f), strokeWidth = lashWidth + 1f)
            scope.drawLine(lashColor, start = Offset(rightEyeCenter.x + eyeSize, rightEyeCenter.y - eyeSize * 0.2f), end = Offset(rightEyeCenter.x + eyeSize * 1.4f, rightEyeCenter.y - eyeSize * 0.6f), strokeWidth = lashWidth + 1f)
        }

        // 5. Eyebrows
        val browColor = parseHexColor(config.eyebrowColorHex)
        val browY = eyeY - eyeSize * 1.1f
        scope.drawLine(browColor, start = Offset(leftEyeCenter.x - eyeSize * 0.9f, browY), end = Offset(leftEyeCenter.x + eyeSize * 0.8f, browY - 3f), strokeWidth = 4f)
        scope.drawLine(browColor, start = Offset(rightEyeCenter.x - eyeSize * 0.8f, browY - 3f), end = Offset(rightEyeCenter.x + eyeSize * 0.9f, browY), strokeWidth = 4f)

        // 6. Blush
        val blushColor = parseHexColor(config.blushColorHex).copy(alpha = config.blushIntensity)
        scope.drawCircle(blushColor, radius = eyeSize * 0.9f, center = Offset(leftEyeCenter.x - eyeSize * 0.2f, eyeY + eyeSize * 1.1f))
        scope.drawCircle(blushColor, radius = eyeSize * 0.9f, center = Offset(rightEyeCenter.x + eyeSize * 0.2f, eyeY + eyeSize * 1.1f))

        // 7. Freckles / Beauty Mark
        if (config.hasFreckles) {
            val freckleColor = Color(0x77795548)
            for (dx in listOf(-12f, -6f, 0f, 6f, 12f)) {
                scope.drawCircle(freckleColor, radius = 2f, center = Offset(headCenter.x + dx, eyeY + eyeSize * 0.9f))
            }
        }
        if (config.hasBeautyMark) {
            scope.drawCircle(Color(0xFF212121), radius = 2.5f, center = Offset(leftEyeCenter.x + eyeSize * 0.8f, eyeY + eyeSize * 1.4f))
        }

        // 8. Nose
        val noseY = eyeY + eyeSize * 1.3f
        val nosePath = Path().apply {
            moveTo(headCenter.x - 3f, noseY)
            lineTo(headCenter.x, noseY + 6f)
            lineTo(headCenter.x + 3f, noseY)
        }
        scope.drawPath(nosePath, color = Color(0xFF8D6E63), style = Stroke(2f))

        // 9. Lips & Mouth
        val mouthY = noseY + eyeSize * 1.2f
        val lipColor = parseHexColor(config.lipstickColorHex)

        val lipWidth = when (config.artStyle) {
            ArtStyle.BRATZ -> eyeSize * 1.6f
            else -> eyeSize * 1.1f
        }

        when (config.expression) {
            EyeExpression.HAPPY, EyeExpression.NEUTRAL -> {
                val mouthPath = Path().apply {
                    moveTo(headCenter.x - lipWidth * 0.5f, mouthY)
                    quadraticTo(headCenter.x, mouthY + 10f, headCenter.x + lipWidth * 0.5f, mouthY)
                }
                scope.drawPath(mouthPath, color = lipColor, style = Stroke(5f, cap = StrokeCap.Round))
                if (config.lipstickFinish == "Gloss Brillante") {
                    scope.drawCircle(Color.White, radius = 3f, center = Offset(headCenter.x, mouthY + 4f))
                }
            }

            else -> {
                scope.drawOval(lipColor, topLeft = Offset(headCenter.x - lipWidth * 0.4f, mouthY - 3f), size = Size(lipWidth * 0.8f, 14f))
            }
        }

        // Face Jewels
        if (config.hasFaceJewels) {
            val jewelColor = parseHexColor(config.faceJewelsHex)
            scope.drawCircle(jewelColor, radius = 4f, center = Offset(headCenter.x, browY - 10f))
            scope.drawCircle(jewelColor, radius = 3f, center = Offset(leftEyeCenter.x, browY - 10f))
            scope.drawCircle(jewelColor, radius = 3f, center = Offset(rightEyeCenter.x, browY - 10f))
        }
    }

    // Clothing (Bottoms & Tops & Full Outfits)
    fun drawClothing(scope: DrawScope, config: AvatarConfig, canvasSize: Size) {
        val w = canvasSize.width
        val h = canvasSize.height
        val chestTop = h * 0.45f
        val chestBottom = h * 0.75f
        val shoulderWidth = w * 0.32f

        val topColor = parseHexColor(config.topColorHex)
        val bottomColor = parseHexColor(config.bottomColorHex)

        if (config.fullOutfitId != "none") {
            // Full Outfit Drawing
            val outfitColor = parseHexColor(config.topColorHex)
            val dressPath = Path().apply {
                moveTo(w * 0.5f - shoulderWidth * 0.5f, chestTop)
                lineTo(w * 0.5f + shoulderWidth * 0.5f, chestTop)
                lineTo(w * 0.5f + shoulderWidth * 0.9f, chestBottom + h * 0.15f)
                lineTo(w * 0.5f - shoulderWidth * 0.9f, chestBottom + h * 0.15f)
                close()
            }
            scope.drawPath(dressPath, color = outfitColor)
            return
        }

        // 1. Bottoms
        when (config.bottomClothingId) {
            "bottom_pleated_skirt" -> {
                val skirtPath = Path().apply {
                    moveTo(w * 0.5f - shoulderWidth * 0.42f, chestBottom - h * 0.1f)
                    lineTo(w * 0.5f + shoulderWidth * 0.42f, chestBottom - h * 0.1f)
                    lineTo(w * 0.5f + shoulderWidth * 0.7f, chestBottom + h * 0.12f)
                    lineTo(w * 0.5f - shoulderWidth * 0.7f, chestBottom + h * 0.12f)
                    close()
                }
                scope.drawPath(skirtPath, color = bottomColor)
            }

            "bottom_mom_jeans", "bottom_cargo" -> {
                val jeansPath = Path().apply {
                    moveTo(w * 0.5f - shoulderWidth * 0.4f, chestBottom - h * 0.1f)
                    lineTo(w * 0.5f + shoulderWidth * 0.4f, chestBottom - h * 0.1f)
                    lineTo(w * 0.5f + shoulderWidth * 0.45f, chestBottom + h * 0.2f)
                    lineTo(w * 0.5f - shoulderWidth * 0.45f, chestBottom + h * 0.2f)
                    close()
                }
                scope.drawPath(jeansPath, color = bottomColor)
            }
        }

        // 2. Tops
        when (config.topClothingId) {
            "top_crop" -> {
                scope.drawRect(
                    color = topColor,
                    topLeft = Offset(w * 0.5f - shoulderWidth * 0.48f, chestTop),
                    size = Size(shoulderWidth * 0.96f, h * 0.12f)
                )
            }

            "top_hoodie", "top_leather_jacket" -> {
                scope.drawRoundRect(
                    color = topColor,
                    topLeft = Offset(w * 0.5f - shoulderWidth * 0.55f, chestTop),
                    size = Size(shoulderWidth * 1.1f, h * 0.22f),
                    cornerRadius = CornerRadius(15f, 15f)
                )
            }

            else -> { // Graphic Tee / Blouse
                scope.drawRect(
                    color = topColor,
                    topLeft = Offset(w * 0.5f - shoulderWidth * 0.5f, chestTop),
                    size = Size(shoulderWidth, h * 0.18f)
                )
            }
        }

        // Footwear
        val shoesColor = parseHexColor(config.shoesColorHex)
        val shoesY = chestBottom + h * 0.18f
        scope.drawRoundRect(shoesColor, topLeft = Offset(w * 0.5f - shoulderWidth * 0.4f, shoesY), size = Size(shoulderWidth * 0.35f, 25f), cornerRadius = CornerRadius(8f, 8f))
        scope.drawRoundRect(shoesColor, topLeft = Offset(w * 0.5f + shoulderWidth * 0.05f, shoesY), size = Size(shoulderWidth * 0.35f, 25f), cornerRadius = CornerRadius(8f, 8f))
    }

    // Front Hair & Bangs
    fun drawFrontHair(scope: DrawScope, config: AvatarConfig, canvasSize: Size) {
        val w = canvasSize.width
        val h = canvasSize.height
        val headCenter = Offset(w * 0.5f, h * 0.28f)
        val hairColor = parseHexColor(config.hairBaseColorHex)
        val streakColor = parseHexColor(config.hairStreakColorHex)

        val hairWidth = w * 0.36f

        // Front Bangs
        when (config.bangsStyleId) {
            "bangs_straight" -> {
                val bangsPath = Path().apply {
                    moveTo(headCenter.x - hairWidth * 0.8f, headCenter.y - h * 0.15f)
                    lineTo(headCenter.x + hairWidth * 0.8f, headCenter.y - h * 0.15f)
                    lineTo(headCenter.x + hairWidth * 0.8f, headCenter.y - h * 0.02f)
                    lineTo(headCenter.x - hairWidth * 0.8f, headCenter.y - h * 0.02f)
                    close()
                }
                scope.drawPath(bangsPath, color = hairColor)
                if (config.hasHairStreaks) {
                    scope.drawRect(streakColor, topLeft = Offset(headCenter.x - 10f, headCenter.y - h * 0.15f), size = Size(20f, h * 0.13f))
                }
            }

            "bangs_curtain" -> {
                val leftCurtain = Path().apply {
                    moveTo(headCenter.x - hairWidth * 0.8f, headCenter.y - h * 0.15f)
                    lineTo(headCenter.x, headCenter.y - h * 0.15f)
                    lineTo(headCenter.x - hairWidth * 0.7f, headCenter.y + h * 0.02f)
                    close()
                }
                val rightCurtain = Path().apply {
                    moveTo(headCenter.x, headCenter.y - h * 0.15f)
                    lineTo(headCenter.x + hairWidth * 0.8f, headCenter.y - h * 0.15f)
                    lineTo(headCenter.x + hairWidth * 0.7f, headCenter.y + h * 0.02f)
                    close()
                }
                scope.drawPath(leftCurtain, color = hairColor)
                scope.drawPath(rightCurtain, color = hairColor)
            }
        }
    }

    // Accessories: Tiara, Glasses, Tech
    fun drawAccessories(scope: DrawScope, config: AvatarConfig, canvasSize: Size) {
        val w = canvasSize.width
        val h = canvasSize.height
        val headCenter = Offset(w * 0.5f, h * 0.28f)

        // 1. Head Accessories
        when (config.headAccessoryId) {
            "acc_tiara" -> {
                val tiaraY = headCenter.y - h * 0.15f
                val tiaraPath = Path().apply {
                    moveTo(headCenter.x - w * 0.12f, tiaraY)
                    lineTo(headCenter.x - w * 0.06f, tiaraY - 20f)
                    lineTo(headCenter.x, tiaraY - 35f)
                    lineTo(headCenter.x + w * 0.06f, tiaraY - 20f)
                    lineTo(headCenter.x + w * 0.12f, tiaraY)
                    close()
                }
                scope.drawPath(tiaraPath, color = Color(0xFFFFD700))
                scope.drawCircle(Color(0xFF00E5FF), radius = 4f, center = Offset(headCenter.x, tiaraY - 35f))
            }

            "acc_cap" -> {
                scope.drawArc(
                    color = Color(0xFF1E88E5),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(headCenter.x - w * 0.22f, headCenter.y - h * 0.2f),
                    size = Size(w * 0.44f, h * 0.2f)
                )
            }
        }

        // 2. Eyewear
        when (config.eyewearAccessoryId) {
            "acc_sunglasses_future" -> {
                val glassY = headCenter.y + h * 0.005f
                scope.drawRoundRect(
                    brush = Brush.horizontalGradient(listOf(Color(0xFFFF007F), Color(0xFF00E5FF))),
                    topLeft = Offset(headCenter.x - w * 0.18f, glassY),
                    size = Size(w * 0.36f, h * 0.045f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
            }
        }

        // 3. Tech / Held Accessories
        when (config.techAccessoryId) {
            "acc_phone" -> {
                // Smartphone in hand
                scope.drawRoundRect(
                    color = Color(0xFF212121),
                    topLeft = Offset(w * 0.72f, h * 0.58f),
                    size = Size(w * 0.08f, h * 0.12f),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                // Screen
                scope.drawRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFFFF4081), Color(0xFF7C4DFF))),
                    topLeft = Offset(w * 0.725f, h * 0.585f),
                    size = Size(w * 0.07f, h * 0.11f)
                )
            }

            "acc_coffee" -> {
                // Coffee Cup
                scope.drawRect(
                    color = Color.White,
                    topLeft = Offset(w * 0.7f, h * 0.62f),
                    size = Size(w * 0.08f, h * 0.09f)
                )
                // Cup Sleeve
                scope.drawRect(
                    color = Color(0xFF6D4C41),
                    topLeft = Offset(w * 0.7f, h * 0.64f),
                    size = Size(w * 0.08f, h * 0.04f)
                )
            }
        }
    }

    // Ambient Lighting Filter
    fun drawAmbientLighting(scope: DrawScope, lighting: AmbientLighting, canvasSize: Size) {
        if (lighting == AmbientLighting.NONE) return
        val color = parseHexColor(lighting.overlayColorHex)
        scope.drawRect(color = color, size = canvasSize)

        // Sparkle effects for Starry Glow
        if (lighting == AmbientLighting.STARRY_GLOW) {
            val sparkles = listOf(
                Offset(canvasSize.width * 0.2f, canvasSize.height * 0.15f),
                Offset(canvasSize.width * 0.8f, canvasSize.height * 0.25f),
                Offset(canvasSize.width * 0.15f, canvasSize.height * 0.7f),
                Offset(canvasSize.width * 0.85f, canvasSize.height * 0.8f)
            )
            for (sp in sparkles) {
                scope.drawCircle(Color.White.copy(alpha = 0.8f), radius = 5f, center = sp)
            }
        }
    }
}
