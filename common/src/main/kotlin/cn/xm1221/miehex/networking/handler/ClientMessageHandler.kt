package cn.xm1221.miehex.networking.handler

import dev.architectury.networking.NetworkManager.PacketContext
import cn.xm1221.miehex.config.MiehexServerConfig
import cn.xm1221.miehex.networking.msg.*

fun MiehexMessageS2C.applyOnClient(ctx: PacketContext) = ctx.queue {
    when (this) {
        is MsgSyncConfigS2C -> {
            MiehexServerConfig.onSyncConfig(serverConfig)
        }

        // add more client-side message handlers here
    }
}
