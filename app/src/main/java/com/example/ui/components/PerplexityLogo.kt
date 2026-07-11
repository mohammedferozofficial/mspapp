package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PerplexityLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    petalColor: Color = MaterialTheme.colorScheme.primary,
    speedMs: Int = 8000 // Smooth, slow professional rotation
) {
    val infiniteTransition = rememberInfiniteTransition(label = "perplexity_logo_rotation")
    
    // Smooth infinite rotation from 0 to 360 degrees
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = speedMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(
        modifier = modifier.size(size)
    ) {
        val center = Offset(size.toPx() / 2f, size.toPx() / 2f)
        
        // Draw the 6 intersecting symmetric capsules/petals
        // Rotate the entire Canvas by the animated angle
        rotate(rotationAngle, center) {
            val petalWidth = size.toPx() * 0.12f
            val petalHeight = size.toPx() * 0.85f
            
            // Loop 6 times, rotating each petal by 30-degree intervals (30, 90, 150...) to form a balanced star
            for (i in 0 until 6) {
                val angle = i * 30f
                rotate(angle, center) {
                    drawRoundRect(
                        color = petalColor,
                        topLeft = Offset(
                            x = center.x - (petalWidth / 2f),
                            y = center.y - (petalHeight / 2f)
                        ),
                        size = Size(petalWidth, petalHeight),
                        cornerRadius = CornerRadius(petalWidth / 2f, petalWidth / 2f),
                        alpha = 0.95f
                    )
                }
            }
        }
    }
}
