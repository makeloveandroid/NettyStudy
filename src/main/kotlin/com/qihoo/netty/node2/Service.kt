package com.qihoo.netty.node2

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.socket.oio.OioServerSocketChannel
import kotlinx.coroutines.*
import java.net.ServerSocket
import java.net.Socket


fun main() = runBlocking<Unit> {
    // 创建管理连接 Group
    val accpet = NioEventLoopGroup()
    // 创建管理数据到来的 Group
    val worker = NioEventLoopGroup()

    ServerBootstrap()
        .group(accpet, worker)
        // 设置NIO模式,若要设置BIO 传入 OioServerSocketChannel
        .channel(NioServerSocketChannel::class.java)
        // 表示是否开启TCP底层心跳机制，true为开启
        .childOption(ChannelOption.SO_KEEPALIVE, true)
        // 表示是否开启Nagle算法，true表示关闭，false表示开 启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减 少发送次数减少网络交互，就开启
        .childOption(ChannelOption.TCP_NODELAY, false)
        //  handler 服务器创过程的逻辑
        .handler(object : ChannelInitializer<NioServerSocketChannel>() {
            override fun initChannel(ch: NioServerSocketChannel?) {
                println("服务器启动中...")
            }
        })
        // childHandler 当有客户端连接进来的时候会调用
        .childHandler(object : ChannelInitializer<NioSocketChannel>() {
            override fun initChannel(ch: NioSocketChannel?) {
                println("有客户进入了")
            }
        })
        .bind(8088)
        .addListener {
            if (it.isSuccess) {
                println("绑定端口成功")
            } else {
                println("绑定端口失败")
            }
        }
}



