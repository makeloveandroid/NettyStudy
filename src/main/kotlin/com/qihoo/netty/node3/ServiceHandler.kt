package com.qihoo.netty.node3

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.nio.charset.Charset
import java.util.*


class ServiceHandler : ChannelInboundHandlerAdapter() {
    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
        println("channelActive  连接成功")
    }

    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        super.channelRegistered(ctx)
        println("channelRegistered  绑定程完成")

    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        val byteBuf = msg as ByteBuf

        println("channelRead  客户端数据来啦:" + byteBuf.toString(Charset.forName("utf-8")))

        // 返回数据
        ctx?.alloc()?.buffer()?.writeBytes("我收到数据啦!!".toByteArray()).let {
            // 发送数据
            ctx?.channel()?.writeAndFlush(it)
        }
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        super.channelReadComplete(ctx)
        println("channelReadComplete  数据读取完毕!")
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        super.channelInactive(ctx)
        println("channelInactive 连接断开")
    }

    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        super.channelUnregistered(ctx)
        println("channelUnregistered 取消线程绑定")
    }

    override fun channelWritabilityChanged(ctx: ChannelHandlerContext?) {
        super.channelWritabilityChanged(ctx)
        println("channelWritabilityChanged 通道可行性改变")
    }
}