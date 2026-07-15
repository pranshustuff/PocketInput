package com.example.pocketinput.network

import com.example.pocketinput.network.Discovery
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

object Connect {

    fun connect(receiver: Discovery.DiscoverReply): TCPClient? {
        val socket = Socket()

        return try {
            socket.tcpNoDelay = true

            socket.connect(
                InetSocketAddress(
                    receiver.ip,
                    receiver.port
                ),
                2000
            )

            TCPClient(socket)

        } catch (_: IOException) {
            socket.close()
            null
        }
    }

    fun discoverAndConnect(
        discoveryPort: Int
    ): Pair<Discovery.DiscoverReply, TCPClient>? {

        val receiver =
            Discovery.findReceiver(discoveryPort)
                ?: return null

        val client =
            connect(receiver)
                ?: return null

        return receiver to client
    }
}
