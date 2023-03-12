package com.fivetfourx.udp_relay


import android.util.Log
import java.net.InetSocketAddress

val prefsKey = "udp relay"
val defaultRelay = "127.0.0.1:5300"
val defaultRemote = "1.1.1.1:53"

fun parseAddress(input: String): InetSocketAddress {
    var split = input.split(":")
    if (split.size != 2) split = listOf("", "")
    val (host, port) = split

    return try {
        InetSocketAddress(host, port.toInt())
    } catch (e: IllegalArgumentException) {
        InetSocketAddress("0.0.0.0", 32767)
    }
}

fun validateAddress(input: String): Boolean {
    var split = input.split(":")
    if (split.size != 2) return false
    val (host, port) = split
    if (!port.matches("[0-9]+".toRegex())) return false
    if (host.length > 253) return false
    val elements = host.split(".")
    for (element in elements) {
        if (!element.matches("[a-zA-Z0-9-]*".toRegex())) return false
        if (element.length > 63) return false
    }
    return true
}

fun log(msg: String) {
    Log.d("ENDLESS-SERVICE", msg)
}
