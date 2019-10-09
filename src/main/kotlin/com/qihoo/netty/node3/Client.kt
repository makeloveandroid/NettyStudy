package com.qihoo.netty.node3

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.thread

fun main() = runBlocking<Unit> {
    val work = NioEventLoopGroup()
    Bootstrap()
        // 设置工作线程组
        .group(work)
        // 设置使用NIO模式,若要BIO 使用 OioSocketChannel
        .channel(NioSocketChannel::class.java)
        // 表示连接的超时时间，超过这个时间还是建 立不上的话则代表连接失败
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
        // 表示是否开启 TCP 底层心跳机制，true 为开启
        .option(ChannelOption.SO_KEEPALIVE, true)
        // 表示是否开始 Nagle 算法，true 表示关闭，false 表示 开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就设置为 true 关 闭，如果需要减少发送次数减少网络交互，就设置为 false 开启
        .option(ChannelOption.TCP_NODELAY, true)
        .handler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel?) {
                println("连接服务中...")
                // 增加一个连接处理器
                ch?.pipeline()?.addLast(ClientHandler())
            }
        })
        // 连接
        .connect("127.0.0.1", 8088)
        // 注册连接回调
        .addListener {
            if (it.isSuccess) {
                println("连接成功!")
                // 连接成功后,可以获取到这个连接的管道 Channel 对象,通过这个对象进行写数据
                val channel = (it as ChannelFuture).channel()
                // 开启线程写数据
                startMsgThread(channel)

            } else {
                println("连接失败!")
            }
        }
}

fun startMsgThread(channel: Channel) {
    thread {
        while (true) {
            println("请输入发送内容：")
            val msg = readLine() ?: ""
            if (!msg.isBlank()) {
                // 判断连接是否正常
                if (channel.isOpen) {
                    // 获取二进制抽象 ByteBuf
                    val buffer = channel.alloc().buffer()
                    // 将数据转换成 bytes 发送
                    buffer.writeBytes(msg.toByteArray())

                    // 发送数据 大家有没有发现 writeAndFlush 是一个 发送是一个 Any 对象?为何要这样设计呢?
                    channel.writeAndFlush(buffer)
                } else {
                    println("连接异常")
                    break
                }
            }else{
                println("请输入发送内容")
            }
        }
    }
}

