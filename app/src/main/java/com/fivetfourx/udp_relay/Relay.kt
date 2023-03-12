/*
Copyright 2023 5t4x

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted, provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED “AS IS” AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
*/

// Based on https://github.com/fragtion/python-udp-relay/blob/master/udp-relay.py
package com.fivetfourx.udp_relay


import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * @param relay The address for the relay to listen to.
 * @param remote The remote server to forward packets to.
 */
class RelayUDP(private val relayStr: String, private val remoteStr: String) {
    private var socket: DatagramSocket? = null
    private var executorService: ExecutorService? = null
    private var running = false

    fun start() {
        running = true
        executorService = Executors.newSingleThreadExecutor()
        executorService?.submit {
            var relay = parseAddress(relayStr)
            var remote = parseAddress(remoteStr)

            socket = DatagramSocket(relay)
            var knownClient: InetSocketAddress? = null

            while (running) {
                val buffer = ByteBuffer.allocate(32768)
                val packet = DatagramPacket(buffer.array(), buffer.capacity())
                socket?.receive(packet)

                val sender = packet.socketAddress as InetSocketAddress

                // The first non-remote message identifies the known client.
                if (sender != knownClient && sender != remote) {
                    knownClient = sender
                }

                if (sender == knownClient) {
                    packet.socketAddress = remote
                } else {
                    packet.socketAddress = knownClient
                }

                socket?.send(packet)
            }
        }
    }

    fun stop() {
        running = false
        socket?.close()
        executorService?.shutdown()
    }
}
