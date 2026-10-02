package cn.xm1221.miehex.networking

import dev.architectury.networking.NetworkChannel
import cn.xm1221.miehex.MieHexMod
import cn.xm1221.miehex.networking.msg.MiehexMessageCompanion

object MiehexNetworking {
    val CHANNEL: NetworkChannel = NetworkChannel.create(MieHexMod.id("networking_channel"))

    fun init() {
        for (subclass in MiehexMessageCompanion::class.sealedSubclasses) {
            subclass.objectInstance?.register(CHANNEL)
        }
    }
}
