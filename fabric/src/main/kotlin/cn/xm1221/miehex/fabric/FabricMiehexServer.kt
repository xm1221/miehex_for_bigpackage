package cn.xm1221.miehex.fabric

import cn.xm1221.miehex.Miehex
import net.fabricmc.api.DedicatedServerModInitializer

object FabricMiehexServer : DedicatedServerModInitializer {
    override fun onInitializeServer() {
        Miehex.initServer()
    }
}
