package com.qihoo.netty.node1

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.ServerSocket
import java.net.Socket


fun main() = runBlocking<Unit> {
    launch {
        // 启动 Socket 绑定端口 8088
        val serverSocket = ServerSocket(8088)
        withContext(Dispatchers.Default) {
            while (true) {
                // (1) 阻塞方法获取新的连接
                println("等待连接...")
                val socket = serverSocket.accept()
                println("用户连接:$socket")
                // (2) 每一个新的连接都创建一个线程，负责读取数据
                withContext<Unit>(Dispatchers.IO) {
                    val data = ByteArray(1024)
                    socket.getInputStream().apply {
                        var len = -1
                        while (read(data).also { len = it } != -1) {
                            println("收到数据:${String(data, 0, len)}")
                        }
                    }

                }
            }
        }
    }
}



