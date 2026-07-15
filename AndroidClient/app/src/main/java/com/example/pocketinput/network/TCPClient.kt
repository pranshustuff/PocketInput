package com.example.pocketinput.network

import java.io.PrintWriter
import java.net.Socket
import com.example.pocketinput.controller.ControllerState
import kotlinx.serialization.json.Json

class TCPClient(
    private val socket: Socket
) {
    private val writer =
        PrintWriter(socket.getOutputStream(), true)

    fun send(state: ControllerState) {
        writer.println(json.encodeToString(state))
    }

    fun disconnect() {
        writer.close()
        socket.close()
    }
}