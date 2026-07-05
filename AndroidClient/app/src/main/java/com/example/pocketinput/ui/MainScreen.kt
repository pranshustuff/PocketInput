package com.example.pocketinput.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pocketinput.controller.ControllerState
import com.example.pocketinput.network.TCPClient
import com.example.pocketinput.ui.controls.GameButton
import com.example.pocketinput.ui.controls.Joystick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val RECEIVER_IP = "10.78.238.132"
private const val RECEIVER_PORT = 5000
private val client = TCPClient()

@Composable
fun MainScreen() {
    val scope = rememberCoroutineScope()
    var controllerState by remember { mutableStateOf(ControllerState()) }
    var isConnected by remember { mutableStateOf(false) }

    LaunchedEffect(controllerState) {
        if (isConnected) {
            scope.launch(Dispatchers.IO) {
                client.send(controllerState)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // TOP LAYER: Connect (+) | Start | Back | Disconnect
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    isConnected = client.connect(RECEIVER_IP, RECEIVER_PORT)
                }
            }) {
                Text(if (isConnected) "Connected" else "+")
            }

            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                GameButton(
                    text = "Back", color = Color.DarkGray,
                    onPressed = { controllerState = controllerState.copy(back = true) },
                    onReleased = { controllerState = controllerState.copy(back = false) },
                    shape = RoundedCornerShape(50),
                    size = 70.dp,
                    fontSize = 14.sp
                )
                GameButton(
                    text = "Start", color = Color.DarkGray,
                    onPressed = { controllerState = controllerState.copy(start = true) },
                    onReleased = { controllerState = controllerState.copy(start = false) },
                    shape = RoundedCornerShape(50),
                    size = 70.dp,
                    fontSize = 14.sp
                )
            }

            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    client.disconnect()
                    isConnected = false
                }
            }) {
                Text("Off")
            }
        }

        // LAYER 1: LT | RT (Centered)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameButton(
                text = "LT", color = Color.Red,
                onPressed = { controllerState = controllerState.copy(lt = 255) },
                onReleased = { controllerState = controllerState.copy(lt = 0) }
            )
            Spacer(modifier = Modifier.width(120.dp))
            GameButton(
                text = "RT", color = Color.Red,
                onPressed = { controllerState = controllerState.copy(rt = 255) },
                onReleased = { controllerState = controllerState.copy(rt = 0) }
            )
        }

        // LAYER 2: LB | RB (Centered)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            GameButton(
                text = "LB", color = Color.Gray,
                onPressed = { controllerState = controllerState.copy(lb = true) },
                onReleased = { controllerState = controllerState.copy(lb = false) }
            )
            Spacer(modifier = Modifier.width(60.dp))
            GameButton(
                text = "RB", color = Color.Gray,
                onPressed = { controllerState = controllerState.copy(rb = true) },
                onReleased = { controllerState = controllerState.copy(rb = false) }
            )
        }

        // LAYER 3: Joysticks and ABXY
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Left Joystick
            Joystick(
                onMove = { x, y ->
                    controllerState = controllerState.copy(lx = x, ly = y)
                },
                size = 180
            )

            // Right Joystick and ABXY
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                // ABXY Buttons
                Box(modifier = Modifier.size(200.dp)) {
                    GameButton(
                        text = "Y", color = Color.Yellow,
                        onPressed = { controllerState = controllerState.copy(y = true) },
                        onReleased = { controllerState = controllerState.copy(y = false) },
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                    GameButton(
                        text = "A", color = Color.Green,
                        onPressed = { controllerState = controllerState.copy(a = true) },
                        onReleased = { controllerState = controllerState.copy(a = false) },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                    GameButton(
                        text = "X", color = Color.Blue,
                        onPressed = { controllerState = controllerState.copy(x = true) },
                        onReleased = { controllerState = controllerState.copy(x = false) },
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                    GameButton(
                        text = "B", color = Color.Red,
                        onPressed = { controllerState = controllerState.copy(b = true) },
                        onReleased = { controllerState = controllerState.copy(b = false) },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }

                // Right Joystick
                Joystick(
                    onMove = { x, y ->
                        controllerState = controllerState.copy(rx = x, ry = y)
                    },
                    size = 150
                )
            }
        }
    }
}
