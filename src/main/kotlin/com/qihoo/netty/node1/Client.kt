package com.qihoo.netty.node1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.Socket
import java.util.*

fun main() = runBlocking<Unit> {
    val socket = Socket("127.0.0.1", 8088)
    withContext(Dispatchers.Default) {
        while (true) {
            println("输入消息发送至客户端: ")
            val sc = Scanner(System.`in`)
            val line = sc.nextLine()
            socket.getOutputStream().write(line.toByteArray())
        }
    }
}