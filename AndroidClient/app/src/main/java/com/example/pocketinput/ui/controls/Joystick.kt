package com.example.pocketinput.ui.controls

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.*

@Composable
fun Joystick(
    onMove: (x: Int, y: Int) -> Unit,
    modifier: Modifier = Modifier,
    size: Int = 150,
) {
    val sizeDp = size.dp
    val density = LocalDensity.current
    val radius = with(density) { (size / 2).dp.toPx() }
    val knobRadius = radius * 0.4f

    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.DarkGray, Color.Black)
                )
            )
            .border(2.dp, Color.Gray, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Inner Circle (Guide)
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = radius * 0.8f,
                center = center
            )
        }

        // Joystick Knob
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(offset.x.roundToInt(), offset.y.roundToInt())
                }
                .size(with(density) { (knobRadius * 2).toDp() })
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.LightGray, Color.Gray)
                    )
                )
                .border(1.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val newOffset = offset + dragAmount
                            val distance = sqrt(newOffset.x.pow(2) + newOffset.y.pow(2))
                            
                            offset = if (distance <= radius) {
                                newOffset
                            } else {
                                val angle = atan2(newOffset.y, newOffset.x)
                                Offset(
                                    cos(angle) * radius,
                                    sin(angle) * radius
                                )
                            }
                            
                            // Normalize to -32768 to 32767 with increased sensitivity
                            // Sensitivity multiplier (1.5x) makes it reach max value sooner
                            // Y is positive when moving down (matching screen coords)
                            val sensitivityMultiplier = 1.5f
                            val rawX = (offset.x / radius * 32767 * sensitivityMultiplier).toInt()
                            val rawY = (offset.y / radius * 32767 * sensitivityMultiplier).toInt()
                            
                            onMove(
                                rawX.coerceIn(-32768, 32767),
                                rawY.coerceIn(-32768, 32767)
                            )
                        },
                        onDragEnd = {
                            offset = Offset.Zero
                            onMove(0, 0)
                        }
                    )
                }
        )
    }
}
