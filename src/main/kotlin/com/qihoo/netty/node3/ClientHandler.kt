package com.qihoo.netty.node3

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.nio.charset.Charset

class ClientHandler : ChannelInboundHandlerAdapter() {
    override fun channelActive(ctx: ChannelHandlerContext?) {
        super.channelActive(ctx)
    }

    override fun channelRegistered(ctx: ChannelHandlerContext?) {
        super.channelRegistered(ctx)
        println("channelRegistered  绑定程完成")

    }

    // 思考 msg 为何也是 Any 类型
    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        (msg as ByteBuf).apply {
            println("收到服务器数据:${toString(Charset.defaultCharset())}")
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