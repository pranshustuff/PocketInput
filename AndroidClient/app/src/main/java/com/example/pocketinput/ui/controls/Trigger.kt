package com.example.pocketinput.ui.controls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

@Composable
fun Trigger(
    label: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Red
) {
    var triggerValue by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .width(50.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color.DarkGray, Color.Black)
                )
            )
            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val delta = dragAmount.y / size.height
                        triggerValue = min(1f, max(0f, triggerValue + delta))
                        onValueChange((triggerValue * 255).toInt())
                    },
                    onDragEnd = {
                        triggerValue = 0f
                        onValueChange(0)
                    }
                )
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(triggerValue)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.4f), color)
                    )
                )
        )
        
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
    }
}
