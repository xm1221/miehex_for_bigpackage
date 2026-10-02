package cn.xm1221.miehex.forge

import cn.xm1221.miehex.MieHexMod
import cn.xm1221.miehex.forge.datagen.ForgeMiehexDatagen
import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(MieHexMod.MOD_ID)
class ForgeMiehex {
    init {
        MOD_BUS.apply {
            EventBuses.registerModEventBus(MieHexMod.MOD_ID, this)
            addListener(ForgeMiehexClient::init)
            addListener(ForgeMiehexDatagen::init)
            addListener(ForgeMiehexServer::init)
        }
        MieHexMod.init()
    }
}
