package cn.xm1221.miehex.networking.msg

import dev.architectury.networking.NetworkChannel
import dev.architectury.networking.NetworkManager.PacketContext
import cn.xm1221.miehex.MieHexMod
import cn.xm1221.miehex.networking.MiehexNetworking
import cn.xm1221.miehex.networking.handler.applyOnClient
import cn.xm1221.miehex.networking.handler.applyOnServer
import net.fabricmc.api.EnvType
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import java.util.function.Supplier

sealed interface MiehexMessage

sealed interface MiehexMessageC2S : MiehexMessage {
    fun sendToServer() {
        MiehexNetworking.CHANNEL.sendToServer(this)
    }
}

sealed interface MiehexMessageS2C : MiehexMessage {
    fun sendToPlayer(player: ServerPlayer) {
        MiehexNetworking.CHANNEL.sendToPlayer(player, this)
    }

    fun sendToPlayers(players: Iterable<ServerPlayer>) {
        MiehexNetworking.CHANNEL.sendToPlayers(players, this)
    }
}

sealed interface MiehexMessageCompanion<T : MiehexMessage> {
    val type: Class<T>

    fun decode(buf: FriendlyByteBuf): T

    fun T.encode(buf: FriendlyByteBuf)

    fun apply(msg: T, supplier: Supplier<PacketContext>) {
        val ctx = supplier.get()
        when (ctx.env) {
            EnvType.SERVER, null -> {
                MieHexMod.LOGGER.debug("Server received packet from {}: {}", ctx.player.name.string, this)
                when (msg) {
                    is MiehexMessageC2S -> msg.applyOnServer(ctx)
                    else -> MieHexMod.LOGGER.warn("Message not handled on server: {}", msg::class)
                }
            }
            EnvType.CLIENT -> {
                MieHexMod.LOGGER.debug("Client received packet: {}", this)
                when (msg) {
                    is MiehexMessageS2C -> msg.applyOnClient(ctx)
                    else -> MieHexMod.LOGGER.warn("Message not handled on client: {}", msg::class)
                }
            }
        }
    }

    fun register(channel: NetworkChannel) {
        channel.register(type, { msg, buf -> msg.encode(buf) }, ::decode, ::apply)
    }
}
