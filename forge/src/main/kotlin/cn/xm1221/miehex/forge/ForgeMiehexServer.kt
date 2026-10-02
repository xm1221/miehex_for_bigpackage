package cn.xm1221.miehex.forge

import cn.xm1221.miehex.Miehex
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent

object ForgeMiehexServer {
    @Suppress("UNUSED_PARAMETER")
    fun init(event: FMLDedicatedServerSetupEvent) {
        Miehex.initServer()
    }
}
