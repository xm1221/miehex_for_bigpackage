package cn.xm1221.miehex.fabric

import cn.xm1221.miehex.MieHexMod
import net.fabricmc.api.ModInitializer

object FabricMiehex : ModInitializer {
    override fun onInitialize() {
        MieHexMod.init()
    }
}
