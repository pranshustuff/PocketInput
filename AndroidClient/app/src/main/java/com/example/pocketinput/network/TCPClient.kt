package com.example.pocketinput.network

import java.io.PrintWriter
import java.net.Socket
import com.example.pocketinput.controller.ControllerState
import kotlinx.serialization.json.Json

class TCPClient {

    private var socket: Socket? = null
    private var writer: PrintWriter? = null

    private val json = Json {
        encodeDefaults = true
    }

    fun connect(ip: String, port: Int): Boolean {
        if (socket != null) return true

        return try {
            val s = Socket(ip, port)
            socket = s
            writer = PrintWriter(s.getOutputStream(), true)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun send(state: ControllerState) {
        writer?.println(json.encodeToString(state))
    }

    fun disconnect() {
        writer?.close()
        socket?.close()

        writer = null
        socket = null
    }
}