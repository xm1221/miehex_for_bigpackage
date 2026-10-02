package cn.xm1221.miehex.fabric

import cn.xm1221.miehex.MieHexMod
import net.fabricmc.api.ClientModInitializer

object FabricMiehexClient : ClientModInitializer {
    override fun onInitializeClient() {
        MieHexMod.initClient()
    }
}
