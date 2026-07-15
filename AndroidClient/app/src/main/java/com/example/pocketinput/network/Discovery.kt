package com.example.pocketinput.network

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketTimeoutException

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.util.Log
import java.net.NetworkInterface

private const val SERVICE = "PocketInput"
private const val PROTOCOL = 1

val json = Json {
    encodeDefaults = true
}

object Discovery {

    @Serializable
    data class DiscoverPacket(
        val service: String = SERVICE,
        val type: String = "discover",
        val protocol: Int = PROTOCOL
    )

    @Serializable
    data class DiscoverReply(
        val service: String,
        val type: String,
        val deviceId: String,
        val deviceName: String,
        val protocol: Int,
        val port: Int,
        var ip: String = ""
    )

    fun findReceiver(port: Int): DiscoverReply? {


        DatagramSocket().use { socket ->

            socket.broadcast = true
            socket.soTimeout = 2000

            val packet = DiscoverPacket()

            val discover = json.encodeToString(
                DiscoverPacket.serializer(),
                packet
            )

            val sendData = discover.toByteArray()
            val interfaces = NetworkInterface.getNetworkInterfaces()

            val broadcasts = mutableSetOf<InetAddress>()

            for (iface in NetworkInterface.getNetworkInterfaces()) {
                if (!iface.isUp || iface.isLoopback) continue

                for (addr in iface.interfaceAddresses) {
                    addr.broadcast?.let { broadcasts += it }
                }
            }

            for (broadcast in broadcasts) {
                socket.send(
                    DatagramPacket(
                        sendData,
                        sendData.size,
                        broadcast,
                        port
                    )
                )
            }


            val receiveBuffer = ByteArray(1024)
            val receivePacket = DatagramPacket(receiveBuffer, receiveBuffer.size)

            try {
                socket.receive(receivePacket)
            } catch (_: SocketTimeoutException) {
                return null
            }

            val replyJson = String(
                receivePacket.data,
                0,
                receivePacket.length
            )

            val reply = json.decodeFromString<DiscoverReply>(replyJson)

            if (
                reply.type == "discover_reply" &&
                reply.protocol == PROTOCOL
            ) {
                return reply.copy(
                    ip = requireNotNull(receivePacket.address.hostAddress)
                )
            }
        }

        return null
    }
}