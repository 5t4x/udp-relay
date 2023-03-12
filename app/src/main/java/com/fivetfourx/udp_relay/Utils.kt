package com.fivetfourx.udp_relay


import android.util.Log
import java.net.InetSocketAddress

val prefsKey = "udp relay"
val defaultRelay = "127.0.0.1:5300"
val defaultRemote = "1.1.1.1:53"

fun parseInput(input: String): InetSocketAddress {
    var split = input.split(":")
    if (split.size != 2) split = listOf("", "")
    val (host, port) = split

    return try {
        InetSocketAddress(host, port.toInt())
    } catch (e: IllegalArgumentException) {
        InetSocketAddress("0.0.0.0", 32767)
    }
}

fun log(msg: String) {
    Log.d("ENDLESS-SERVICE", msg)
}
