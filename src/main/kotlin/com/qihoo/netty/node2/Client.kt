package com.qihoo.netty.node2

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.Socket
import java.util.*
import java.util.concurrent.TimeUnit

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
        .option(ChannelOption.TCP_NODELAY, false)
        .handler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(ch: SocketChannel?) {
                println("连接服务中...")

            }
        })
        // 连接
        .connect("127.0.0.1", 8088)
        // 注册连接回调
        .addListener {
            if (it.isSuccess) {
                println("连接成功!")

            } else {
                println("连接失败!")
            }
        }
}

class CheckIdleStateHandler : IdleStateHandler(0, 0, 0, TimeUnit.SECONDS) {
    override fun channelIdle(ctx: ChannelHandlerContext?, evt: IdleStateEvent?) {
        super.channelIdle(ctx, evt)
    }

}